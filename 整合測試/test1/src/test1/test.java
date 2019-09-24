package test1;

public class test
{
	public static void main(String[] args) throws Exception
	{
		ClassDivider cd = new ClassDivider();
		
		int year = 106;                      // 學年
		int term = 1;                        // 學期 (1上學期 2下學期)
		String serial_num = "ACS";           // 課程代號 EX.ACS
		String class_name = "測試課程";      // 課程名稱 (中文)
		short necessary = 1;                 // 必修 = 1 ,選修 = 0
		float point = 3;                     // 學分
		float score = 99;                    // 分數, 正在修未取得分數之課程 = -1
		
		ClassInfo testClass = new ClassInfo(year,term,serial_num,class_name,necessary,point,score);
		cd.classify(testClass);
		cd.print();
	}
	
}
