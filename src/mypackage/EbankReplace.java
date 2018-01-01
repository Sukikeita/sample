import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 网银系统源文件清单转换成编译后文件清单
 * @author bfadmin
 *
 */
public class EbankReplace {
	/**
	 * 
	 * @param fileListPath 源文件清单路径
	 * @return 源文件路径集合
	 */
	public List<String> getList(String fileListPath){
		List<String> list = new ArrayList<String>();
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(new File(fileListPath)));
			String line = null;
			while ((line=br.readLine())!=null) {
				list.add(line.trim());
			}
			return list;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}finally{
			if(br!=null){
				try {
					br.close();
				} catch (IOException e) {
					throw new RuntimeException(e);		
				}
			}
		}
	}	
	
	/**
	 *  * 
	 * @param projectName 项目名称
	 * @param fileListPath	源文件清单路径
	 * @param factoryName   /designFiles/bizs/后面的路径名称
	 */
	public void fileList(String projectName, String factoryName,String fileListPath){
		List<String> list = getList(fileListPath);
		List<String> filelist = new ArrayList<String>();
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new FileWriter(new File(fileListPath)));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		for (String filePath : list) {
			String separator = "/";
			if(!filePath.startsWith("/")) {
				separator ="";
			}
			if(filePath.indexOf(separator + projectName + "/designFiles/bizs/" + factoryName + "/serverFlow.xml")!=-1){
				filelist.add(separator + projectName + "/WebContent/WEB-INF/bizs/" + factoryName + "/operations.xml");
			}else if(filePath.indexOf(separator + projectName + "/designFiles/bizs/" + factoryName + "/")!=-1 && filePath.endsWith(".biz")){
				filePath = filePath.substring(filePath.lastIndexOf("/") + 1).replace(".biz",".xml");
				filelist.add(separator + projectName + "/WebContent/WEB-INF/bizs/" + factoryName + "/operations/" + filePath);
			}else if(filePath.indexOf(separator + projectName + "/designFiles/mvcs/" + factoryName + "/")!=-1 && filePath.endsWith(".mvc")){
				filePath = filePath.substring(filePath.lastIndexOf("/") + 1).replace(".biz",".xml");
				filelist.add(separator + projectName + "/WebContent/WEB-INF/mvcs/" + factoryName + "/actions/" + filePath);
			}else if (filePath.indexOf(separator + projectName + "/JavaSource/")!=-1) {
				filelist.add(filePath.replace(separator + projectName + "/JavaSource/", separator + projectName + "/WebContent/WEB-INF/classes/").replace(".java", ".class"));
			}else if (filePath.indexOf(separator + projectName + "/designFiles/")!=-1){
				filelist.add(filePath.replace(separator + projectName + "/designFiles/", separator + projectName + "/WebContent/WEB-INF/"));
			}else {
				filelist.add(filePath);
			}
		}
		
		try {
			for (String filePath : filelist) {
				bw.write(filePath);
				bw.newLine();
			} 
		}catch (IOException e) {
			throw new RuntimeException(e);
		}finally{
			try {
				if(bw!=null)
					bw.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
			
	}
	
	public static void main(String[] args) {
		EbankReplace er = new EbankReplace();
		er.fileList(args[0], args[1],args[2]);
	}
}
