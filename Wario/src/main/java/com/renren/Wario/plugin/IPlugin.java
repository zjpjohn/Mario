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
package com.renren.Wario.plugin;

import com.renren.Wario.mailsender.IMailSender;
import com.renren.Wario.msgsender.IMsgSender;
import com.renren.Wario.zookeeper.ZooKeeperClient;

public abstract class IPlugin implements Runnable {

	public IMsgSender msgSender = null;
	public IMailSender mailSender = null;
	public ZooKeeperClient client = null;
	public byte[] clusterContext;

	public String[] numbers;
	public String[] addresses;
	
	public String[] args = null;
}
