package com.chen;

import com.chen.model.TencentMovie;
import com.chen.utils.TencentMovieSpider;

import java.util.List;

/**
 * Created by ChenQiang on 2017/10/12.
 */
public class SpiderTest {

    public static void main(String[] args) {
        List<TencentMovie> movies = TencentMovieSpider.spider();
        System.out.println(movies);
    }
}
