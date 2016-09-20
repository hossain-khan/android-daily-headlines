package io.swagger.client.api;

import io.swagger.client.CollectionFormats.*;

import rx.Observable;

import retrofit2.http.*;

import okhttp3.RequestBody;

import io.swagger.client.model.InlineResponse200;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface StoriesApi {
  /**
   * Top Stories
   * The Top Stories API provides JSON and JSONP lists of articles and associated images by section. 
   * @param section The section the story appears in. (required)
   * @param format if this is JSONP or JSON (required)
   * @param callback The name of the function the API call results will be passed to. Required when using JSONP. This parameter has only one valid value per section. The format is {section_name}TopStoriesCallback.  (optional)
   * @return Call&lt;InlineResponse200&gt;
   */
  
  @GET("{section}.{format}")
  Observable<InlineResponse200> sectionFormatGet(
    @Path("section") String section, @Path("format") String format, @Query("callback") String callback
  );

}
