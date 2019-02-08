package at.gepardec.hogarama.scaling;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.management.ManagementFactory;

import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectInstance;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gepardec.hogarama.domain.metrics.Metrics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebServlet("/ScalingTest")
public class ScalingTestServlet extends HttpServlet {
	
	private static final long serialVersionUID = -4257270508512265869L;
	private static final Logger logger = LoggerFactory.getLogger(ScalingTestServlet.class);

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
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
				Metrics.exceptionsThrown.labels("hogajama-frontend", e.getClass().toString(), "ScalingTestServlet.getActiveSessions").inc();
				throw new RuntimeException(e);
			}
        return activeSessions;
	}
}
