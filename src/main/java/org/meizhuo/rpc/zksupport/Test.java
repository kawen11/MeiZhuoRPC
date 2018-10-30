package org.meizhuo.rpc.zksupport;

import java.io.IOException;
import java.util.List;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;

public class Test implements Watcher {

	private ZooKeeper zk;
	public void connectZk(String host,int times) throws IOException{
		zk = new ZooKeeper(host,times,this);
	}
	public void printStatus(){
		System.out.println(zk.getState());
	}
	public void createNode(String node,String value) throws KeeperException, InterruptedException{

		if (null==zk.exists(node, false)){
			if(null == value){
				zk.create(node,null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
			}else{
				zk.create(node,value.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
			}
		} else {
			System.out.println("Already exists....");
		}
	}
	public List<String> getNode(String node) throws KeeperException, InterruptedException{
		List<String> children= zk.getChildren(node,this);
		System.out.println("List:"+children);
        return children;
	}
	@Override
	public void process(WatchedEvent arg0) {
		System.out.println("changing...........");
	}

	public static void main(String[] args) throws IOException, KeeperException, InterruptedException {
		Test test = new Test();
		test.connectZk("127.0.0.1:2181", 2000);
		test.printStatus();
		String node = "/test1/ooo";
		test.createNode(node, "hello world!");
		String parentNode="/test1";
		test.getNode(parentNode);
		test.createNode("/test2/child", null);
	}
}
