# StoriesApi

All URIs are relative to *http://api.nytimes.com/svc/topstories/v2/*

Method | HTTP request | Description
------------- | ------------- | -------------
[**sectionFormatGet**](StoriesApi.md#sectionFormatGet) | **GET** {section}.{format} | Top Stories


<a name="sectionFormatGet"></a>
# **sectionFormatGet**
> InlineResponse200 sectionFormatGet(section, format, callback)

Top Stories

The Top Stories API provides JSON and JSONP lists of articles and associated images by section. 

### Example
```java
// Import classes:
//import io.swagger.client.ApiClient;
//import io.swagger.client.ApiException;
//import io.swagger.client.Configuration;
//import io.swagger.client.auth.*;
//import io.swagger.client.api.StoriesApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure API key authorization: apikey
ApiKeyAuth apikey = (ApiKeyAuth) defaultClient.getAuthentication("apikey");
apikey.setApiKey("YOUR API KEY");
// Uncomment the following line to set a prefix for the API key, e.g. "Token" (defaults to null)
//apikey.setApiKeyPrefix("Token");

StoriesApi apiInstance = new StoriesApi();
String section = "section_example"; // String | The section the story appears in.
String format = "format_example"; // String | if this is JSONP or JSON
String callback = "callback_example"; // String | The name of the function the API call results will be passed to. Required when using JSONP. This parameter has only one valid value per section. The format is {section_name}TopStoriesCallback. 
try {
    InlineResponse200 result = apiInstance.sectionFormatGet(section, format, callback);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling StoriesApi#sectionFormatGet");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **section** | **String**| The section the story appears in. | [enum: home, opinion, world, national, politics, upshot, nyregion, business, technology, science, health, sports, arts, books, movies, theater, sundayreview, fashion, tmagazine, food, travel, magazine, realestate, automobiles, obituaries, insider]
 **format** | **String**| if this is JSONP or JSON | [enum: json, jsonp]
 **callback** | **String**| The name of the function the API call results will be passed to. Required when using JSONP. This parameter has only one valid value per section. The format is {section_name}TopStoriesCallback.  | [optional]

### Return type

[**InlineResponse200**](InlineResponse200.md)

### Authorization

[apikey](../README.md#apikey)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

