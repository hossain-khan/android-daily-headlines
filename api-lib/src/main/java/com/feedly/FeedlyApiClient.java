package com.feedly;
/*
 * MIT License
 *
 * Copyright (c) 2016 Hossain Khan
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */



import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.Map;

import io.swagger.client.auth.ApiKeyAuth;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;


public class FeedlyApiClient {

    private Map<String, Interceptor> apiAuthorizations;
    private OkHttpClient.Builder okBuilder;
    private Retrofit.Builder adapterBuilder;

    public FeedlyApiClient() {
        apiAuthorizations = new LinkedHashMap<String, Interceptor>();
        createDefaultAdapter();
    }

    public FeedlyApiClient(String[] authNames) {
        this();
        for(String authName : authNames) { 
            throw new RuntimeException("auth name \"" + authName + "\" not found in available auth names");
        }
    }

    /**
     * Basic constructor for single auth name
     * @param authName Authentication name
     */
    public FeedlyApiClient(String authName) {
        this(new String[]{authName});
    }

    /**
     * Helper constructor for single api key
     * @param authName Authentication name
     * @param apiKey API key
     */
    public FeedlyApiClient(String authName, String apiKey) {
        this(authName);
        this.setApiKey(apiKey);
    }


    public void createDefaultAdapter() {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
                .registerTypeAdapter(DateTime.class, new DateTimeTypeAdapter())
                .registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter())
                .create();

        okBuilder = new OkHttpClient.Builder();

        String baseUrl = "http://cloud.feedly.com/v3";
        if(!baseUrl.endsWith("/"))
        	baseUrl = baseUrl + "/";

        adapterBuilder = new Retrofit
                .Builder()
                .baseUrl(baseUrl)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonCustomConverterFactory.create(gson));
    }

    public <S> S createService(Class<S> serviceClass) {
        return adapterBuilder
            .client(okBuilder.build())
            .build()
            .create(serviceClass);

    }

    /**
     * Helper method to configure the first api key found
     * @param apiKey API key
     */
    private void setApiKey(String apiKey) {
        for(Interceptor apiAuthorization : apiAuthorizations.values()) {
            if (apiAuthorization instanceof ApiKeyAuth) {
                ApiKeyAuth keyAuth = (ApiKeyAuth) apiAuthorization;
                keyAuth.setApiKey(apiKey);
                return;
            }
        }
    }


    /**
     * Adds an authorization to be used by the client
     * @param authName Authentication name
     * @param authorization Authorization interceptor
     */
    public void addAuthorization(String authName, Interceptor authorization) {
        if (apiAuthorizations.containsKey(authName)) {
            throw new RuntimeException("auth name \"" + authName + "\" already in api authorizations");
        }
        apiAuthorizations.put(authName, authorization);
        okBuilder.addInterceptor(authorization);
    }

    public Map<String, Interceptor> getApiAuthorizations() {
        return apiAuthorizations;
    }

    public void setApiAuthorizations(Map<String, Interceptor> apiAuthorizations) {
        this.apiAuthorizations = apiAuthorizations;
    }

    public Retrofit.Builder getAdapterBuilder() {
        return adapterBuilder;
    }

    public void setAdapterBuilder(Retrofit.Builder adapterBuilder) {
        this.adapterBuilder = adapterBuilder;
    }

    public OkHttpClient.Builder getOkBuilder() {
        return okBuilder;
    }

    public void addAuthsToOkBuilder(OkHttpClient.Builder okBuilder) {
        for(Interceptor apiAuthorization : apiAuthorizations.values()) {
            okBuilder.addInterceptor(apiAuthorization);
        }
    }

    /**
     * Clones the okBuilder given in parameter, adds the auth interceptors and uses it to configure the Retrofit
     * @param okClient An instance of OK HTTP client
     */
    public void configureFromOkclient(OkHttpClient okClient) {
        this.okBuilder = okClient.newBuilder();
        addAuthsToOkBuilder(this.okBuilder);

    }
}

/**
 * This wrapper is to take care of this case:
 * when the deserialization fails due to JsonParseException and the
 * expected type is String, then just return the body string.
 */
class GsonResponseBodyConverterToString<T> implements Converter<ResponseBody, T> {
	  private final Gson gson;
	  private final Type type;

	  GsonResponseBodyConverterToString(Gson gson, Type type) {
	    this.gson = gson;
	    this.type = type;
	  }

	  @Override public T convert(ResponseBody value) throws IOException {
	    String returned = value.string();
	    try {
	      return gson.fromJson(returned, type);
	    }
	    catch (JsonParseException e) {
                return (T) returned;
        }
	 }
}

class GsonCustomConverterFactory extends Converter.Factory
{
	public static GsonCustomConverterFactory create(Gson gson) {
	    return new GsonCustomConverterFactory(gson);
	  }

	  private final Gson gson;
	  private final GsonConverterFactory gsonConverterFactory;

	  private GsonCustomConverterFactory(Gson gson) {
	    if (gson == null) throw new NullPointerException("gson == null");
	    this.gson = gson;
	    this.gsonConverterFactory = GsonConverterFactory.create(gson);
	  }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        if(type.equals(String.class))
            return new GsonResponseBodyConverterToString<Object>(gson, type);
        else
            return gsonConverterFactory.responseBodyConverter(type, annotations, retrofit);
    }

    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type, Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
            return gsonConverterFactory.requestBodyConverter(type, parameterAnnotations, methodAnnotations, retrofit);
    }
}

/**
 * Gson TypeAdapter for Joda DateTime type
 */
class DateTimeTypeAdapter extends TypeAdapter<DateTime> {

    private final DateTimeFormatter formatter = ISODateTimeFormat.dateTime();

    @Override
    public void write(JsonWriter out, DateTime date) throws IOException {
        if (date == null) {
            out.nullValue();
        } else {
            out.value(formatter.print(date));
        }
    }

    @Override
    public DateTime read(JsonReader in) throws IOException {
        switch (in.peek()) {
            case NULL:
                in.nextNull();
                return null;
            default:
                String date = in.nextString();
                return formatter.parseDateTime(date);
        }
    }
}

/**
 * Gson TypeAdapter for Joda LocalDate type
 */
class LocalDateTypeAdapter extends TypeAdapter<LocalDate> {

    private final DateTimeFormatter formatter = ISODateTimeFormat.date();

    @Override
    public void write(JsonWriter out, LocalDate date) throws IOException {
        if (date == null) {
            out.nullValue();
        } else {
            out.value(formatter.print(date));
        }
    }

    @Override
    public LocalDate read(JsonReader in) throws IOException {
        switch (in.peek()) {
            case NULL:
                in.nextNull();
                return null;
            default:
                String date = in.nextString();
                return formatter.parseLocalDate(date);
        }
    }
}
