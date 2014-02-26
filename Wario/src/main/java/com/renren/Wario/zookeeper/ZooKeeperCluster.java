/**
 *    Copyright 2014 Renren.com
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package com.renren.Wario.zookeeper;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ZooKeeperCluster {

	private JSONObject object = null;
	private Set<String> connectStrings = null;
	private int sessionTimeout;
	private String zookeeperName = null;

	private Map<String, ZooKeeperClient> clients = null;

	public ZooKeeperCluster(String zookeeperName, JSONObject object) {
		this.zookeeperName = zookeeperName;
		this.object = object;
	}

	public void init() {
		clients = new HashMap<String, ZooKeeperClient>();
		clients.clear();
		connectStrings = readJSONObject();
		addCliens(connectStrings);
	}

	public void updateClients(JSONObject object) {
		this.object = object;
		Set<String> newConnectStrings = readJSONObject();
		Set<String> tmp = getIntersection(connectStrings, newConnectStrings);
		deleteCliens(getDifference(connectStrings, tmp));
		addCliens(getDifference(newConnectStrings, tmp));
		connectStrings = newConnectStrings;
	}

	private Set<String> getUnion(Set<String> a, Set<String> b) {
		Set<String> res = new HashSet<String>();
		res.clear();
		res.addAll(a);
		res.addAll(b);
		return res;
	}

	private Set<String> getDifference(Set<String> a, Set<String> b) {
		Set<String> res = new HashSet<String>();
		res.clear();
		res.addAll(a);
		res.removeAll(b);
		return res;
	}

	private Set<String> getIntersection(Set<String> a, Set<String> b) {
		Set<String> res = new HashSet<String>();
		res.clear();
		res.addAll(a);
		res.retainAll(b);
		return res;
	}

	private void addCliens(Set<String> connectStrings) {
		Iterator<String> it = connectStrings.iterator();
		while (it.hasNext()) {
			String connectString = it.next();
			ZooKeeperClient zookeeperClient = new ZooKeeperClient(
					connectString, sessionTimeout);
			clients.put(connectString, zookeeperClient);
			System.err.println("Client add for " + zookeeperName + " : "
					+ connectString);
		}
	}

	private void deleteCliens(Set<String> connectStrings) {
		Iterator<String> it = connectStrings.iterator();
		while (it.hasNext()) {
			String connectString = it.next();
			ZooKeeperClient zookeeperClient = clients.get(connectString);
			zookeeperClient.close();
			clients.remove(connectString);
			System.err.println("Client delete for " + zookeeperName + " : "
					+ connectString);
		}
	}

	private Set<String> readJSONObject() {
		Set<String> res = new HashSet<String>();
		res.clear();
		try {
			JSONArray connectStringArray = object.getJSONArray("serverIPList");
			for (int i = 0; i < connectStringArray.length(); ++i) {
				res.add(connectStringArray.getString(i));
			}
			sessionTimeout = object.getInt("sessionTimeout");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;

	}

}