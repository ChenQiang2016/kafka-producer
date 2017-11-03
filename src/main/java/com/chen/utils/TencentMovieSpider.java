package com.chen.utils;

import com.chen.model.TencentMovie;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class TencentMovieSpider {

    private static String base_url = "http://v.qq.com/x/list/movie?&offset={page}";
    private static String detail_url_prefix = "http://v.qq.com/detail/";

    public static List<TencentMovie> spider() {
        List<TencentMovie> movies = new ArrayList<TencentMovie>();
//        int totalpage = totalPage();
        int totalpage = 2;
        for (int i = 0; i < totalpage; i++) {
            int page = i * 30;
            String url = base_url.replace("{page}", page + "");
            String html = Http.get(url);
            Document doc = Jsoup.parse(html);
            Elements lis = doc.select("ul.figures_list").first().select("li.list_item");
            for (Element ele : lis) {
                String vurl = ele.getElementsByTag("a").first().attr("href");
                String id = vurl.substring(vurl.lastIndexOf("/") + 1);
                String detail_url = detail_url_prefix + id.charAt(0) + "/" + id;
                movies.addAll(parseMovie(detail_url));
            }
        }
        return movies;
    }

    private static List<TencentMovie> parseMovie(String detail_url) {
        List<TencentMovie> movies = new ArrayList<TencentMovie>();
        try {
            String html = Http.get(detail_url);
            Document doc = Jsoup.parse(html);
            Element div = doc.select("div.mod_figure_detail.mod_figure_detail_en.cf").first();
            String title = div.select("div.video_title_collect.cf").first().getElementsByTag("a").first().text();
            String actor = "";
            Elements lis = div.select("ul.actor_list.cf").first().children();
            for (Element e : lis) {
                if (e.toString().contains("<span class=\"director\">导演</span>")) {

                } else {
                    actor += e.select("span.name").first().text() + " ";
                }
            }
            String tag = div.select("div.tag_list").first().text();
            String origin = getParams(div, "span:matches(地*区)");
            String introduction = div.select("span.txt._desc_txt_lineHight").first().text();
            String cover = div.select("img").first().attr("src");
            String playUrl = div.getElementsByTag("a").first().attr("href");
            if (cover.startsWith("//")) {
                cover = "http:" + cover;
            }

            TencentMovie movie = new TencentMovie();
            movie.setActor(actor);
            movie.setImage(cover);
            movie.setDesc(introduction);
            movie.setOrigin(origin);
            movie.setTag(tag);
            movie.setAssetName(title);
            movie.setSpiderUrl(detail_url);
            movie.setPlayUrl(playUrl);

            movies.add(movie);

            System.out.println("==========================" + title + "解析完成===========================");
        } catch (Exception e) {
            System.out.println("==============================解析失败:" + detail_url);
        }
        return movies;
    }

    private static String getParams(Element ele, String reg) {
        Element element = ele.select(reg).first();
        return element == null ? "" : element.parent().getElementsByTag("span").last().text();
    }

    private static int totalPage() {
        int page = 1;
        try {
            String url = base_url.replace("{page}", "0");
            String html = Http.get(url);
            Document doc = Jsoup.parse(html);
            Element div = doc.select("div.mod_pages").first();
            Elements as = div.select("span._items").first().children();
            page = Integer.parseInt(as.get(as.size() - 1).text());
        } catch (Exception e) {
            System.out.println("=======================获取总页数失败=======================");
            e.printStackTrace();
        }
        return page;
    }
}