package com.example.yunan.tripscanner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yunan on 2017-06-08.
 */

public class Review {

    private List<Map<String,Object>> reviews = new ArrayList<Map<String,Object>>();
    private Map<String,Object> review = new HashMap<String, Object>() ;

    public List getReviews(){
        return reviews;
    }
    public Map<String,Object> getReview(){
        return review;
    }

}
