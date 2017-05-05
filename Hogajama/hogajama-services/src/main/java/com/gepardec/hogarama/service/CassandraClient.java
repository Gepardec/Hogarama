package com.gepardec.hogarama.service;

import java.net.InetSocketAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Host;
import com.datastax.driver.core.Metadata;
import com.datastax.driver.core.Session;

public class CassandraClient {

	private static final Logger LOG = LoggerFactory.getLogger(CassandraClient.class);

	private Cluster cluster;
	private Session session;

	// TODO: im standalone.xml konfigurieren
	private String node = "cassandra";
	private Integer port = 9042;

	private void connect() {
		cluster = Cluster.builder().addContactPointsWithPorts(new InetSocketAddress(node, port)).build();

		if (LOG.isDebugEnabled()) {
			Metadata metadata = cluster.getMetadata();
			LOG.debug("Cluster name: " + metadata.getClusterName());
			for (Host host : metadata.getAllHosts()) {
				LOG.debug("Datacenter: " + host.getDatacenter() + " Host: " + host.getAddress() + " Rack: "
						+ host.getRack());
			}
		}

		session = cluster.connect();
	}

	public Session getSession() {
		connect();
		return this.session;
	}

	public void closeSessionAndCluster() {
		if (session != null) {
			try {
				session.close();
			} catch (Exception e) {
				/* ignoriert */
			}
		}
		if (cluster != null) {
			try {
				cluster.close();
			} catch (Exception e) {
				/* ignoriert */
			}
		}
	}
}
