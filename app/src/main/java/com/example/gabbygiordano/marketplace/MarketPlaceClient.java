package com.example.gabbygiordano.marketplace;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

/**
 * Created by tanvigupta on 7/11/17.
 */

public class MarketPlaceClient {
    public static final String GET_URL = "https://api.data.gov/ed/collegescorecard";
    public static final String ENDPOINT = "/v1/schools";
    public static final String FORMAT = ".json?";

    public static final String API_KEY = "api_key=1PVcYE59lhrYLGtQN2FhXppctGHkHE7KvFYgYcPu";

    public static final String OPTION_PARAMS = "&school.region_id__not=9" +
                                               "&school.carnegie_undergrad__range=5..15" +
                                               "&_fields=school.name" +
                                               "&_sort=school.name";

    public static void getSchoolList(AsyncHttpResponseHandler handler) {
        String url = GET_URL + ENDPOINT + FORMAT + API_KEY + OPTION_PARAMS;
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, null, handler);
    }
}
