package at.gepardec.hogarama.scaling;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.management.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.management.ManagementFactory;

@WebServlet("/ScalingTest")
public class ScalingTestServlet extends HttpServlet {
	
	private static final long serialVersionUID = -4257270508512265869L;
	private static final Logger logger = LoggerFactory.getLogger(ScalingTestServlet.class);

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) {
		try{
			PrintWriter out = response.getWriter();

			out.println("Session ID: " + request.getSession().getId());

			if (request.getSession().getAttribute("test") == null) {
				out.println("New session. Current session is empty.");

				request.getSession().setAttribute("test", Integer.valueOf(1));
				logger.info("Here we start with a new session");

			} else {
				Integer value = (Integer) request.getSession().getAttribute("test");
				out.println("Existing session: Value is " + value);

				request.getSession().setAttribute("test",
						Integer.valueOf(value.intValue() + 1));
			}

			out.println("Active Sessions: " + getActiveSession());
			out.println("End");
		} catch(IOException e){
			logger.error("Error handling http request", e);
		}

	}

	private int getActiveSession() {
        MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
        int activeSessions = 0;
            try {
				for (ObjectInstance objectInstance : mBeanServer.queryMBeans(
						new ObjectName("jboss.as:deployment=*,subsystem=undertow"), null)) {
					activeSessions += (Integer) mBeanServer.getAttribute(objectInstance.getObjectName(), "activeSessions");
				}
			} catch (MalformedObjectNameException | AttributeNotFoundException | InstanceNotFoundException
					| MBeanException | ReflectionException e) {
				throw new RuntimeException(e);
			}
        return activeSessions;
	}
}
