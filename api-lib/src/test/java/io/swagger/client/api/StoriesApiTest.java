package io.swagger.client.api;

import io.swagger.client.ApiClient;
import io.swagger.client.model.InlineResponse200;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * API tests for StoriesApi
 */
public class StoriesApiTest {

    private StoriesApi api;

    @Before
    public void setup() {
        api = new ApiClient().createService(StoriesApi.class);
    }

    
    /**
     * Top Stories
     *
     * The Top Stories API provides JSON and JSONP lists of articles and associated images by section. 
     */
    @Test
    public void sectionFormatGetTest() {
        String section = null;
        String format = null;
        String callback = null;
        // InlineResponse200 response = api.sectionFormatGet(section, format, callback);

        // TODO: test validations
    }
    
}
