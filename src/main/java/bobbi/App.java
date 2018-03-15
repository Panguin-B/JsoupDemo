package bobbi;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Hello world!
 */
public class App {

    public static void main(String[] args) throws IOException {

        jsoupMain();

    }

    private static void jsoupMain() throws IOException {

        String url = "https://bing.ioliu.cn";

        Document document = Jsoup.connect(url)
                .userAgent("Mozilla/4.0 (compatible; MSIE 9.0; Windows NT 6.1; Trident/5.0)")
                .get();

        // 添加检索条件
        Elements elements = document.select("div.container").select("div.item").
                select("div.card.progressive").select("img");

        String str = elements.toString();

        for (Element e : elements) {

            String imgUrl = e.absUrl("abs:data-progressive");

            downloadImg(imgUrl);
        }
    }

    private static void downloadImg(String imgUrl) {

        try {

            URL url = new URL(imgUrl);

            System.out.println(url);

            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            // 请求代理服务器
            con.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");

            if (con.getResponseCode() == 200) {

                InputStream is = con.getInputStream();

                File path = new File("/Users/dllo/Desktop/pingbao");

                if (!path.exists() || !path.isDirectory()) {
                    path.mkdirs();
                }

                String fileName = imgUrl.substring(imgUrl.lastIndexOf("/"));

                File file = new File(path, fileName);

                FileOutputStream fos = new FileOutputStream(file);

                int l = 0;

                byte[] buf = new byte[1024];

                if ((l = is.read(buf)) != -1) {
                    fos.write(buf, 0, l);
                }

                fos.close();

                is.close();

            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
