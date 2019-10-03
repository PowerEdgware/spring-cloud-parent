package test;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class UriTest {

	public static void main(String[] args) {
		//uriDemo();
		
		urlDemo();
	}

	private static void uriDemo() {
		try {
			URI uri=new URI("/aim-server/say?a=xxx&b=yyy#c");
			System.out.println(uri.getScheme()+"_"+uri.getHost()+"_"+uri.getPort());
			System.out.println(uri.getPath());
			System.out.println(uri.getQuery());
			System.out.println(uri.getFragment());
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}

	private static void urlDemo() {
		try {
			URL url=new URL("https://www.baidu.com/files/look?p=x&b=y#cc");
			System.out.println(url.getFile());
			System.out.println(url.getProtocol());
			System.out.println(url.getHost());
			System.out.println(url.getPort());
			System.out.println(url.getPath());
			System.out.println(url.getQuery());
			System.out.println(url.getRef());
			System.out.println(url.getContent());
			System.out.println("========URI============");
			URI uri=url.toURI();
			System.out.println(uri.getScheme()+"_"+uri.getHost()+"_"+uri.getPort());
			System.out.println(uri.getPath());
			System.out.println(uri.getQuery());
			System.out.println(uri.getFragment());
			
		} catch (MalformedURLException | URISyntaxException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
