import java.io.*;
import java.net.*;
import java.util.*;
import static java.lang.System.out;

public class ListNIFs 
{
	public static void main(String args[]) throws SocketException {
		Enumeration<NetworkInterface> nets = NetworkInterface.getNetworkInterfaces();
		
		for (NetworkInterface netIf : Collections.list(nets)) {
			out.printf("Display name: %s\n", netIf.getDisplayName());
			out.printf("Name: %s\n", netIf.getName());
      displaySubInterfaces(netIf);
			displayInterfaceInformation(netIf);
			displayMacAddresses(netIf);
			out.printf("\n");
		}
	}

	static void displaySubInterfaces(NetworkInterface netIf) throws SocketException {
		Enumeration<NetworkInterface> subIfs = netIf.getSubInterfaces();
		
		for (NetworkInterface subIf : Collections.list(subIfs)) {
			out.printf("\tSub Interface Display name: %s\n", subIf.getDisplayName());
			out.printf("\tSub Interface Name: %s\n", subIf.getName());
		}
	 }

	 static void displayInterfaceInformation(NetworkInterface netint) throws SocketException {
		Enumeration<InetAddress> inetAddresses = netint.getInetAddresses();
		for (InetAddress inetAddress : Collections.list(inetAddresses)) {
			out.printf("InetAddress: %s\n", inetAddress);
		}
	 }

	 static void displayMacAddresses(NetworkInterface netint) throws SocketException {
		Enumeration<InetAddress> inetAddresses = netint.getInetAddresses();
		for (InetAddress inetAddress : Collections.list(inetAddresses)) {
			byte[] mac = netint.getHardwareAddress();
			if (mac != null){
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < mac.length; i++) {
				sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));        
			}
				out.printf("Mac Address: %s\n", sb.toString());
			}
		}
		out.printf("\n");
	 }


}  