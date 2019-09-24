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
	
	public ClassDivider() //Constructor
	{
		// 布林值是用來判斷是否為該學期的課
		Must = new ClassArray("必修", true); // 必修
		Pro = new ClassArray("專業選修", true); // 專業選修
		Ban = new ClassArray("未過", true); // 沒過 <60分
		NotFit = new ClassArray("不符合自由選修", true); // 不符合自由選修
		Common = new ClassArray("通識", true); // 通識
		ThisTerm = new ClassArray("本學期課程", false); // 正在修
	}

	public void print(int type)
	{
		switch (type)
		{
		case 0:
			Must.print();
			break;
		case 1:
			Pro.print();
			break;
		case 2:
			Ban.print();
			break;
		case 3:
			NotFit.print();
			break;
		case 4:
			Common.print();
			break;
		case 5:
			ThisTerm.print();
			break;
		case 6:
			printGE(); //印出通識修課狀況
			break;
		case 7:
			printPointSituation();
			break;
		case 8:
			printNowPointSituation(); //加上當學期修課後結果
			break;
		}
	}

	public void decode(String term_info, int y, int t) throws Exception // 拆解網頁文字
	{
		BufferedReader br = new BufferedReader(new StringReader(term_info));
		String line;
		String[] Split_test = null;
		Boolean IsGetAllGrade = false;
		ArrayList<String> inputList = new ArrayList<String>();

		while ((line = br.readLine()) != null)
		{
			if (line.length() > 0 && !line.trim().isEmpty())
				inputList.add(line);
		}

		for (int i = 7; i < inputList.size(); i++)
			if (!IsGetAllGrade)
			{
				Split_test = inputList.get(i).split("\\s+");
				if (Split_test[0].equals("學期成績"))
					IsGetAllGrade = true;
				else
				{
					if (Split_test.length == 6)
					{
						// 已修過(分數已經出來），但因為課程名稱中有空白鍵而被拆解多一個字串
						ClassInfo CI = new ClassInfo(y, t, Split_test[0], Split_test[1] + Split_test[2],
								(short) ((Split_test[3].equals("必修")) ? 1 : 0), Float.parseFloat(Split_test[4]),
								Float.parseFloat(Split_test[5]));
						classify(CI);
					} 
					else
					{
						if (Split_test[2].equals("必修") || Split_test[2].equals("選修"))
						{
							if (Split_test.length > 4) // 已修過(分數已經出來
							{
								if(Split_test[4].equals("通過"))
								{
									ClassInfo CI = new ClassInfo(y, t, Split_test[0], Split_test[1],
											(short) ((Split_test[2].equals("必修")) ? 1 : 0), Float.parseFloat(Split_test[3]),
											80);
									classify(CI);
								}
								else if(Split_test[4].equals("不通過"))
								{
									ClassInfo CI = new ClassInfo(y, t, Split_test[0], Split_test[1],
											(short) ((Split_test[2].equals("必修")) ? 1 : 0), Float.parseFloat(Split_test[3]),
											0);
									classify(CI);
								}
								else
								{
									ClassInfo CI = new ClassInfo(y, t, Split_test[0], Split_test[1],
											(short) ((Split_test[2].equals("必修")) ? 1 : 0), Float.parseFloat(Split_test[3]),
											Float.parseFloat(Split_test[4]));
									classify(CI);
								}
							} 
							else
							{// 當學期(分數未出來
								ClassInfo CI = new ClassInfo(y, t, Split_test[0], Split_test[1],
										(short) ((Split_test[2].equals("必修")) ? 1 : 0), Float.parseFloat(Split_test[3]),
										-1);
								classify(CI);
							}
						} 
						else
						{ // 當學期(分數未出來），但因為課程名稱中有空白鍵而被拆解多一個字串
							ClassInfo CI = new ClassInfo(y, t, Split_test[0], Split_test[1] + Split_test[2],
									(short) ((Split_test[3].equals("必修")) ? 1 : 0), Float.parseFloat(Split_test[4]),
									-1);
							classify(CI);
						}
					}
				}
			}
	}

	private void classify(ClassInfo A) throws Exception// 分類傳入陣列
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

	public void GEDecode(String GEInfo) throws Exception //通識課程網頁文字拆解
	{
		BufferedReader br = new BufferedReader(new StringReader(GEInfo));
		String line;
		ArrayList<String> inputList = new ArrayList<String>();
		
		while ((line = br.readLine()) != null)
		{
			if (line.length() > 0 && !line.trim().isEmpty())
				inputList.add(line);
		}
		SHPoint_K = Integer.parseInt(inputList.get(6).substring(12,13));
		SHPoint= Integer.parseInt(inputList.get(6).substring(14,15));
		MSPoint_K = Integer.parseInt(inputList.get(7).substring(12,13));
		MSPoint= Integer.parseInt(inputList.get(7).substring(14,15));
		ASPoint_K = Integer.parseInt(inputList.get(8).substring(12,13));
		ASPoint= Integer.parseInt(inputList.get(8).substring(14,15));
		
		TotalCommonPoint = SHPoint_K+SHPoint+MSPoint_K+MSPoint+ASPoint_K+ASPoint;
		if( TotalCommonPoint > 18) //多的通識學分加入自由學分
		{
			Free += TotalCommonPoint - 18;
			TotalCommonPoint = 18;
		}
	}
	
	public void printPointSituation() 
	{
		if(CommonMust > CommonMustTarget) 
		{
			Free += CommonMust-CommonMustTarget;
			CommonMust = CommonMustTarget;
		}
		if(CommonLanguage >CommonLanguageTarget) 
		{
			Free += CommonLanguage-CommonLanguageTarget;
			CommonLanguage = CommonLanguageTarget;
		}
		if(CollegeMust > CollegeMustTarget) 
		{
			Free += CollegeMust-CollegeMustTarget;
			CollegeMust = CollegeMustTarget;
		}
		if(DepartmentMust > DepartmentMustTarget) 
		{
			Free += DepartmentMust-DepartmentMustTarget;
			DepartmentMust = DepartmentMustTarget;
		}
		if(DepartmentChoose > DepartmentChooseTarget) 
		{
			Free += DepartmentChoose-DepartmentChooseTarget;
			DepartmentChoose = DepartmentChooseTarget;
		}
		if(TotalCommonPoint > 18) 
		{
			Free += TotalCommonPoint- 18;
			TotalCommonPoint = 18;
		}
		
		System.out.print("您目前核可的學分總學分為： " + 
				(CommonMust+CommonLanguage+CollegeMust+DepartmentMust+DepartmentChoose+Free+TotalCommonPoint+LiberalArt)
		+ "學分\n\n");
		System.out.print("系上修課情形：\n");
		if(CollegeMust < CollegeMustTarget) {
			System.out.print("院內必修學分為： "+ CollegeMust + "學分 (離訂定修畢學分"+CollegeMustTarget+"學分，還缺少"+(DepartmentMustTarget-CollegeMust)+"學分)\n");
		}else 
		{
			System.out.print("院內必修學分為： "+ CollegeMust + "學分 (訂定修畢學分"+CollegeMustTarget+"學分，恭喜達成目標!)\n");
		}
		if(DepartmentMust < DepartmentMustTarget) {
			System.out.print("系上必修學分為： "+ DepartmentMust + "學分 (離訂定修畢學分"+DepartmentMustTarget+"學分，還缺少"+(DepartmentMustTarget-DepartmentMust)+"學分)\n");
		}else 
		{
			System.out.print("系上必修學分為： "+ DepartmentMust + "學分 (訂定修畢學分"+DepartmentMustTarget+"學分 ，恭喜達成目標!)\n");
		}
		if(DepartmentChoose < DepartmentChooseTarget) {
			System.out.print("系上專業選修學分為： "+ DepartmentChoose + "學分 (離訂定修畢學分"+DepartmentChooseTarget+"學分，還缺少"+(DepartmentChooseTarget-DepartmentChoose)+"學分)\n");
		}else 
		{
			System.out.print("系上專業選修學分為： "+ DepartmentChoose + "學分 (訂定修畢學分"+DepartmentChooseTarget+"學分， 恭喜達成目標!)\n");
		}
		if(Free < FreeTarget) {
			System.out.print("自由學分為： "+ Free + "學分 (離訂定修畢學分"+FreeTarget+"學分，還缺少"+(FreeTarget-Free)+"學分)\n");
		}else 
		{
			System.out.print("自由學分為： "+ Free + "學分 (訂定修畢學分"+FreeTarget+"學分，恭喜達成目標!)\n");
		}
		
		System.out.print("\n通識修課情形：\n");
		if(CommonMust < CommonMustTarget) {
			System.out.print("共同必修學分為： "+ CommonMust + "學分 (離訂定修畢學分"+CommonMustTarget+"學分，還缺少"+(CommonMustTarget-CommonMust)+"學分)\n");
		}else 
		{
			System.out.print("共同必修學分為： "+ CommonMust + "學分 (訂定修畢學分"+CommonMustTarget+"學分， 恭喜達成目標!)\n");
		}
		
		if(CommonLanguage < CommonLanguageTarget) {
			System.out.print("共同語文通識學分為： "+ CommonLanguage + "學分 (離訂定修畢學分"+CommonLanguage+"學分，還缺少"+(CommonLanguageTarget-CommonLanguage)+"學分)\n");
		}else 
		{
			System.out.print("共同語文通識學分為： "+ CommonLanguage + "學分 (訂定修畢學分"+CommonLanguageTarget+"學分，恭喜達成目標!)\n");
		}
		System.out.print("博雅講堂學分為： "+LiberalArt+"學分(注意：博雅講堂尚未被記入通識選修,請自行計算。)\n");
		System.out.print("通識選修學分：("+TotalCommonPoint+"學分)\n");
		System.out.print("社會人文領域：(核心)"+SHPoint_K+", (非核心)"+SHPoint+"\n");
		System.out.print("數理科技領域：(核心)"+MSPoint_K+", (非核心)"+MSPoint+"\n");
		System.out.print("藝術陶冶領域：(核心)"+ASPoint_K+", (非核心)"+ASPoint+"\n");
		if((SHPoint_K >= 4 && SHPoint_K + SHPoint == 8) &&
				(MSPoint_K >= 2 && MSPoint_K + MSPoint == 6) &&
				(ASPoint_K >= 2 && ASPoint_K + ASPoint == 4))
			{
				System.out.print("(恭喜達到訂定修畢學分!)\n");
			}
			else
			{
				System.out.print("修課建議：\n");
				if(SHPoint_K + SHPoint != 8) //社會人文
				{
					if(SHPoint_K < 4) 
					{
						System.out.print("社會核心缺少"+(2 - SHPoint_K/2) +"門\n");
						if(SHPoint < 4) 
						{
							System.out.print("社會非核心缺少"+(2 - SHPoint/2) +"門\n");
						}
						
					}else if(SHPoint_K >= 4)
					{
						System.out.print("社會非核心缺少"+(4 - (SHPoint_K+SHPoint)/2) +"門\n");
					}
					
				}
				if(MSPoint_K + MSPoint != 6) //數理科技
				{
					if(MSPoint_K < 2) 
					{
						System.out.print("數理核心缺少1門\n");
						if(MSPoint < 4) 
						{
							System.out.print("數理非核心缺少"+(2 - MSPoint/2) +"門\n");
						}
						
					}else if(MSPoint_K >= 2)
					{
						System.out.print("數理非核心缺少"+(3 - (MSPoint_K+MSPoint)/2) +"門\n");
					}
					
				}
				
				if(ASPoint_K + ASPoint != 4) //藝術陶冶
				{
					if(ASPoint_K < 2) 
					{
						System.out.print("藝術核心缺少1門\n");
						
						
					}
					if(ASPoint < 2) 
					{
						System.out.print("藝術非核心缺少1門\n");
					}
					
				}
			}
	}
	
	public void printNowPointSituation() 
	{
		System.out.print("您目前核可的學分總學分為： " + 
				(CommonMust+CommonLanguage+CollegeMust+DepartmentMust+DepartmentChoose+Free+TotalCommonPoint+LiberalArt
						+TermCommonMust+TermCommonLanguage+TermCollegeMust+TermDepartmentMust+TermDepartmentChoose+TermFree+TermCommonChoose+TermLiberalArt)
		+ "學分\n\n");
		
		float tmp_CommonMust = CommonMust + TermCommonMust;
		float tmp_CommonLanguage = CommonLanguage + TermCommonLanguage;
		float tmp_CommonChoose = TotalCommonPoint + TermCommonChoose;
		float tmp_CollegeMust = CollegeMust + TermCollegeMust;
		float tmp_DepartmentMust = DepartmentMust + TermDepartmentMust;
		float tmp_DepartmentChoose = DepartmentChoose + TermDepartmentChoose;
		float tmp_Free = Free + TermFree;
		
		if(tmp_CommonMust > CommonMustTarget) 
		{
			tmp_Free += tmp_CommonMust-CommonMustTarget;
			tmp_CommonMust = CommonMustTarget;
		}
		if(tmp_CommonLanguage >CommonLanguageTarget) 
		{
			tmp_Free += tmp_CommonLanguage-CommonLanguageTarget;
			tmp_CommonLanguage = CommonLanguageTarget;
		}
		if(tmp_CollegeMust > CollegeMustTarget) 
		{
			tmp_Free += tmp_CollegeMust-CollegeMustTarget;
			tmp_CollegeMust = CollegeMustTarget;
		}
		if(tmp_DepartmentMust > DepartmentMustTarget) 
		{
			tmp_Free += tmp_DepartmentMust-DepartmentMustTarget;
			tmp_DepartmentMust = DepartmentMustTarget;
		}
		if(tmp_DepartmentChoose > DepartmentChooseTarget) 
		{
			tmp_Free += tmp_DepartmentChoose-DepartmentChooseTarget;
			tmp_DepartmentChoose = DepartmentChooseTarget;
		}
		if(tmp_CommonChoose > 18) 
		{
			tmp_Free += tmp_CommonChoose- 18;
			tmp_CommonChoose = 18;
		}
		
		System.out.print("系上修課情形：\n");
		if(tmp_CollegeMust < CollegeMustTarget) {
			System.out.print("院內必修學分為： "+ tmp_CollegeMust + "學分 (離訂定修畢學分"+CollegeMustTarget+"學分，還缺少"+(DepartmentMustTarget-tmp_CollegeMust)+"學分)\n");
		}else 
		{
			System.out.print("院內必修學分為： "+ tmp_CollegeMust + "學分 (訂定修畢學分"+CollegeMustTarget+"學分，恭喜達成目標!)\n");
		}
		if(tmp_DepartmentMust < DepartmentMustTarget) {
			System.out.print("系上必修學分為： "+ tmp_DepartmentMust + "學分 (離訂定修畢學分"+DepartmentMustTarget+"學分，還缺少"+(DepartmentMustTarget-tmp_DepartmentMust)+"學分)\n");
		}else 
		{
			System.out.print("系上必修學分為： "+ tmp_DepartmentMust + "學分 (訂定修畢學分"+DepartmentMustTarget+"學分 ，恭喜達成目標!)\n");
		}
		if(tmp_DepartmentChoose < DepartmentChooseTarget) {
			System.out.print("系上專業選修學分為： "+ tmp_DepartmentChoose + "學分 (離訂定修畢學分"+DepartmentChooseTarget+"學分，還缺少"+(DepartmentChooseTarget-tmp_DepartmentChoose)+"學分)\n");
		}else 
		{
			System.out.print("系上專業選修學分為： "+ tmp_DepartmentChoose + "學分 (訂定修畢學分"+DepartmentChooseTarget+"學分， 恭喜達成目標!)\n");
		}
		
		if(tmp_Free < FreeTarget) {
			System.out.print("自由學分為： "+ tmp_Free + "學分 (離訂定修畢學分"+FreeTarget+"學分，還缺少"+(FreeTarget-tmp_Free)+"學分)\n");
		}else 
		{
			System.out.print("自由學分為： "+ tmp_Free + "學分 (訂定修畢學分"+FreeTarget+"學分，恭喜達成目標!)\n");
		}
		
		System.out.print("\n通識修課情形：\n");
		if(tmp_CommonMust < CommonMustTarget) {
			System.out.print("共同必修學分為： "+ tmp_CommonMust + "學分 (離訂定修畢學分"+CommonMustTarget+"學分，還缺少"+(CommonMustTarget-tmp_CommonMust)+"學分)\n");
		}else 
		{
			System.out.print("共同必修學分為： "+ tmp_CommonMust + "學分 (訂定修畢學分"+CommonMustTarget+"學分， 恭喜達成目標!)\n");
		}
		
		if(tmp_CommonLanguage < CommonLanguageTarget) {
			System.out.print("共同語文通識學分為： "+ tmp_CommonLanguage + "學分 (離訂定修畢學分"+CommonLanguageTarget+"學分，還缺少"+(CommonLanguageTarget-tmp_CommonLanguage)+"學分)\n");
		}else 
		{
			System.out.print("共同語文通識學分為： "+ tmp_CommonLanguage + "學分 (訂定修畢學分"+CommonLanguageTarget+"學分，恭喜達成目標!)\n");
		}
		System.out.print("博雅講堂學分為： "+(LiberalArt+TermLiberalArt)+"學分 (注意：博雅講堂尚未被記入通識選修,請自行計算。)\n");
		System.out.print("通識選修學分：("+tmp_CommonChoose+"學分");
		if(tmp_CommonChoose >= 18)
			System.out.print("，恭喜達成目標!");
		System.out.print(")\n");
	}
	
	private void printGE() 
	{
		System.out.print("------------------------\n");
		System.out.print("|　　　　　　|核心|非核心|\n");
		System.out.print("------------------------\n");
		System.out.print("|社會人文領域|　 "+SHPoint_K+"|　    "+SHPoint+"|\n");
		System.out.print("------------------------\n");
		System.out.print("|數理科技領域|　 "+MSPoint_K+"|　    "+MSPoint+"|\n");
		System.out.print("------------------------\n");
		System.out.print("|藝術陶冶領域|　 "+ASPoint_K+"|　    "+ASPoint+"|\n");
		System.out.print("------------------------\n");
	}
}
