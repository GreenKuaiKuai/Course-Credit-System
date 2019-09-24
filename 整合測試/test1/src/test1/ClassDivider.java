package test1;
import java.io.*;
import java.util.ArrayList;

public class ClassDivider
{
	private ClassArray Must; // 必修
	private ClassArray Pro; // 專業選修
	private ClassArray Ban; // 沒過 <60分
	private ClassArray NotFit; // 不符合自由選修
	private ClassArray Common; // 通識
	private ClassArray ThisTerm; // 正在修

	private int SHPoint_K; //社會人文領域-核心
	private int SHPoint; //社會人文領域-非核
	private int MSPoint_K; //數理科技領域-核心
	private int MSPoint; //數理科技領域-非核
	private int ASPoint_K; //藝術陶冶領域-核心
	private int ASPoint; //藝術陶冶領域-非核
	private int TotalCommonPoint;
	
	
	//資工系目標學分
	private float CommonMustTarget = 2; //共同必修學分(體育)
	private float CommonLanguageTarget = 8; //共同語文通識學分
	private float CollegeMustTarget = 9; //院必修學分
	private float DepartmentMustTarget = 41; //系定必修學分
	private float DepartmentChooseTarget = 30; //系定選修學分
	private float FreeTarget = 20; //自由選修學分
	
	//使用者當前學分
	private float CommonMust = 0; //共同必修學分
	private float CommonLanguage = 0; //共同語文通識學分
	private float CollegeMust = 0; //院必修學分
	private float DepartmentMust = 0; //系定必修學分
	private float DepartmentChoose = 0; //系定選修學分
	private float Free = 0; //自由選修學分
	private float LiberalArt = 0;
	
	//本學期修課後累加得到學分
	private float TermCommonMust = 0; //共同必修學分
	private float TermCommonLanguage = 0; //共同語文通識學分
	private float TermCommonChoose = 0; //通識選修
	private float TermCollegeMust = 0; //院必修學分
	private float TermDepartmentMust = 0; //系定必修學分
	private float TermDepartmentChoose = 0; //系定選修學分
	private float TermFree = 0; //自由選修學分
	private float TermLiberalArt = 0;
	
	public ClassDivider() //Constructory
	{
		// 布林值是用來判斷是否為該學期的課
		Must = new ClassArray("必修", true); // 必修
		Pro = new ClassArray("專業選修", true); // 專業選修
		Ban = new ClassArray("未過", true); // 沒過 <60分
		NotFit = new ClassArray("不符合自由選修", true); // 不符合自由選修
		Common = new ClassArray("通識", true); // 通識
		ThisTerm = new ClassArray("本學期課程", false); // 正在修
	}

	public void print()//印出所有資料庫結果
	{
		Must.print();
		Pro.print(); 
		Ban.print();  
		NotFit.print();
		Common.print();
		ThisTerm.print();
	}

	public void classify(ClassInfo A) throws Exception// 分類傳入陣列
	{
		// 讀取系上承認的自由選修
		FileReader fr = new FileReader("free.txt");
		BufferedReader br = new BufferedReader(fr);
		ArrayList<String> FreeList = new ArrayList<String>();
		String line = new String();

		while ((line = br.readLine()) != null)
		{
			if (line.length() > 0 && !line.trim().isEmpty())
				FreeList.add(line);
		}
		br.close();
		fr.close();

		// 開始判斷
		if (A.getScore() >= 60)
		{
			if (A.getNecessary() == 1)// 必修
			{
				String N = A.getSerialNum().substring(0, 3);
				if (N.equals("ACS") || N.equals("ZCS") || N.equals("AGE"))
				{
					if(N.equals("ACS"))  //檢查是否為系上必修
					{
						DepartmentMust += A.getPoint();
					}
					else if(N.equals("ZCS"))  //檢查是否為院必修
					{
						CollegeMust += A.getPoint(); 
					}
					else if(N.equals("AGE")) 
					{
						if(A.getClassName().length() >= 4)
						{
							String CN =  A.getClassName().substring(0,4);
							if(CN.equals("大一體育") || CN.equals("大二體育")) 
							{
								//檢查是否為體育課
								CommonMust += A.getPoint();
							}
							if(CN.equals("博雅講堂")) 
							{
								LiberalArt += A.getPoint();
							}
						}
						if(A.getClassName().length() >= 2) 
						{
							String CN = A.getClassName().substring(0,2);
							if(CN.equals("國文") || CN.equals("英文"))
							{
								//檢查是否為語文課
								CommonLanguage += A.getPoint(); 
							}
						}
					}
					Must.add(A);
				}
				else
					NotFit.add(A);
			} 
			else// 選修
			{
				if (A.getSerialNum().substring(0, 3).equals("ACS"))
				{
					if(DepartmentChoose < DepartmentChooseTarget) //系上選修若超過門檻，加到自由選修學分
					{
						DepartmentChoose += A.getPoint();
						if(DepartmentChoose > DepartmentChooseTarget) //加完學分後，若超過系上選修門檻,多餘的移至自由選修
						{
							Free += DepartmentChoose - DepartmentMustTarget;
							DepartmentChoose = DepartmentMustTarget;
						}
						
					}else
					{
						Free += A.getPoint();
					}
					Pro.add(A);
				}
				else if (A.getSerialNum().substring(0, 3).equals("AGE")) //採計學校通識頁面裁定之分數
				{
					Common.add(A);
					if(A.getClassName().length() >= 4)
					{
						String CN =  A.getClassName().substring(0,4);
						if(CN.equals("博雅講堂")) 
						{
							LiberalArt += A.getPoint();
						}
					}
				}
				else
				{
					boolean IsFind = false;
					for (int i = 0; i < FreeList.size(); i++) 
					{
						if (FreeList.get(i).equals(A.getClassName()))// 符合自由選修
						{
							Pro.add(A);
							Free += A.getPoint();
							IsFind = true;
						}
					}
					if(!IsFind) 
					{
						NotFit.add(A);// 不符合自由選修
					}
				}
			}
		} 
		else
		{
			if (A.getScore() == -1)// 正在修
			{
				if (A.getNecessary() == 1)// 必修
				{
					String N = A.getSerialNum().substring(0, 3);
					if (N.equals("ACS") || N.equals("ZCS") || N.equals("AGE"))
					{
						if(N.equals("ACS"))  //檢查是否為系上必修
						{
							TermDepartmentMust += A.getPoint();
						}
						else if(N.equals("ZCS"))  //檢查是否為院必修
						{
							TermCollegeMust += A.getPoint(); 
						}
						else if(N.equals("AGE")) 
						{
							if(A.getClassName().length() >= 4)
							{
								String CN =  A.getClassName().substring(0,4);
								if(CN.equals("大一體育") || CN.equals("大二體育")) 
								{
									//檢查是否為體育課
									TermCommonMust += A.getPoint();
								}
							}
							if(A.getClassName().length() >= 2) 
							{
								String CN = A.getClassName().substring(0,2);
								if(CN.equals("國文") || CN.equals("英文"))
								{
									//檢查是否為語文課
									TermCommonLanguage += A.getPoint(); 
								}
							}
						}
					}
				}
				else// 選修
				{
					if (A.getSerialNum().substring(0, 3).equals("ACS"))
					{
						TermDepartmentChoose += A.getPoint();
					}
					else if (A.getSerialNum().substring(0, 3).equals("AGE")) //採計學校通識頁面裁定之分數
					{
						//需要判別核心非核心
						TermCommonChoose += A.getPoint();
						if(A.getClassName().length() >= 4)
						{
							String CN =  A.getClassName().substring(0,4);
							if(CN.equals("博雅講堂")) 
							{
								TermLiberalArt += A.getPoint();
							}
						}
					}
					else
					{
						for (int i = 0; i < FreeList.size(); i++) 
						{
							if (FreeList.get(i).equals(A.getClassName()))// 符合自由選修
							{
								TermFree += A.getPoint();
							}
						}
					}
				}
				ThisTerm.add(A);
			}
			else
				Ban.add(A);// 不及格沒修過
		}
	}

}
