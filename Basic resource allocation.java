package org.cloudbus.cloudsim.examples;
import org.cloudbus.cloudsim.*; 
import org.cloudbus.cloudsim.core.CloudSim; 
import org.cloudbus.cloudsim.provisioners.*; 
import java.util.*; 
public class BasicResourceAllocation { 
 public static void main(String[] args) { 
 int numUsers = 1; 
 Calendar calendar = Calendar.getInstance(); 
 boolean traceFlag = false; 
 CloudSim.init(numUsers, calendar, traceFlag); 
 Datacenter datacenter = 
 createDatacenter("Datacenter_0"); 
 DatacenterBroker broker = createBroker(); 
 int brokerId = broker.getId(); 
 List<Vm> vmList = new ArrayList<>(); 
 int vmCount = 4; 
 for (int i = 0; i < vmCount; i++) {  
 Vm vm = new Vm( 
 i, 
 brokerId, 
 1000, 
 1, 
 1024, 
 1000, 
 10000, 
 "Xen", 
 new CloudletSchedulerTimeShared() 
 ); 
 vmList.add(vm); 
 } 
 broker.submitVmList(vmList); 
 List<Cloudlet> cloudletList = new ArrayList<>(); 
 int cloudletCount = 8; 
 for (int i = 0; i < cloudletCount; i++) { 
 Cloudlet cloudlet = new Cloudlet( 
 i, 
 40000, 
 1, 
 300, 
 300, 
 new UtilizationModelFull(), 
 new UtilizationModelFull(), 
 new UtilizationModelFull() 
 ); 
 cloudlet.setUserId(brokerId); 
 cloudletList.add(cloudlet); 
 } 
 broker.submitCloudletList(cloudletList); 
 CloudSim.startSimulation(); 
 CloudSim.stopSimulation(); 
 List<Cloudlet> resultList = 
 broker.getCloudletReceivedList(); 
 printCloudletResults(resultList); 
 } 
 private static Datacenter createDatacenter(String name) { 
 List<Host> hostList = new ArrayList<>(); 
 int hostCount = 4; 
 for (int i = 0; i < hostCount; i++) { 
 int hostId = i; 
 int ram = 2048; 
 long storage = 1000000; 
 int bw = 10000; 
 List<Pe> peList = new ArrayList<>(); 
 peList.add( 
 new Pe( 
 0, 
 new PeProvisionerSimple(1000) 
 ) 
 ); 
 Host host = new Host( 
 hostId, 
 new RamProvisionerSimple(ram), 
 new BwProvisionerSimple(bw), 
 storage, 
 peList, 
 new VmSchedulerTimeShared(peList) 
 ); 
 hostList.add(host); 
 } 
 String arch = "x86"; 
 String os = "Linux"; 
 String vmm = "Xen"; 
 double time_zone = 10.0; 
 double cost = 3.0; 
 double costPerMem = 0.05; 
 double costPerStorage = 0.001; 
 double costPerBw = 0.0; 
 DatacenterCharacteristics characteristics = 
 new DatacenterCharacteristics( 
 arch, 
 os, 
 vmm, 
 hostList, 
 time_zone, 
 cost, 
 costPerMem, 
 costPerStorage, 
 costPerBw 
 ); 
 Datacenter datacenter = null; 
 try { 
 datacenter = new Datacenter( 
 name, 
 characteristics, 
 new VmAllocationPolicySimple(hostList), 
 new LinkedList<Storage>(), 
 0 
 ); 
 } catch (Exception e) { 
 e.printStackTrace(); 
 } 
 return datacenter; 
 } 
 private static DatacenterBroker createBroker() { 
 DatacenterBroker broker = null; 
 try { 
 broker = new DatacenterBroker("Broker"); 
 } catch (Exception e) { 
 e.printStackTrace(); 
 } 
 return broker; 
 } 
 private static void printCloudletResults( 
 List<Cloudlet> list) { 
 String indent = " "; 
 System.out.println( 
 "Cloudlet ID" + indent + 
 "STATUS" + indent + 
 "Data center ID" + indent + 
 "VM ID" + indent + 
 "Time" + indent + 
 "Start Time" + indent + 
 "Finish Time" 
 ); 
 for (Cloudlet cloudlet : list) { 
 System.out.print( 
 indent + 
 cloudlet.getCloudletId() + 
 indent + indent 
 ); 
 if (cloudlet.getStatus() == Cloudlet.SUCCESS) { 
 System.out.println( 
 "SUCCESS" + indent + indent + 
 cloudlet.getResourceId() + indent + indent + 
 cloudlet.getVmId() + indent + indent + 
 cloudlet.getActualCPUTime() + indent + 
 cloudlet.getExecStartTime() + indent + indent + 
 cloudlet.getFinishTime() 
 ); 
 } 
 } 
 } 
}