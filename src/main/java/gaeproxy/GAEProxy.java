package gaeproxy;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;

public class GAEProxy extends HttpServlet {

	private static final long serialVersionUID = -4685945835054414900L;

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		proxy(req, resp);
	}

	private void proxy(HttpServletRequest req, HttpServletResponse resp) throws MalformedURLException, IOException, ProtocolException {
		String requestURI = req.getRequestURI();

		URL url = new URL("https://script.google.com" + requestURI);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setDoOutput(true);
		connection.setRequestMethod("GET");

		// resp.addHeader("x-frame-options", "IGNORE");

		for (Map.Entry<String, List<String>> entry : connection.getHeaderFields().entrySet()) {

			if (entry.getKey().equalsIgnoreCase(("x-frame-options"))) {
				continue;
			}

			for (String value : entry.getValue()) {
				resp.addHeader(entry.getKey(), value);
			}
		}

		IOUtils.copy(connection.getInputStream(), resp.getOutputStream());

	}
}