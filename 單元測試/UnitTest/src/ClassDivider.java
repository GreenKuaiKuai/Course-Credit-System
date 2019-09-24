import java.io.*;
import java.util.ArrayList;

public class ClassDivider
{
	private ClassArray Must; // ����
	private ClassArray Pro; // �M�~���
	private ClassArray Ban; // �S�L <60��
	private ClassArray NotFit; // ���ŦX�ۥѿ��
	private ClassArray Common; // �q��
	private ClassArray ThisTerm; // ���b��
	
	
	//��u�t�ؼоǤ�
	private float CommonMustTarget = 2; //�@�P���׾Ǥ�(��|)
	private float CommonLanguageTarget = 8; //�@�P�y��q�ѾǤ�
	private float CollegeMustTarget = 9; //�|���׾Ǥ�
	private float DepartmentMustTarget = 41; //�t�w���׾Ǥ�
	private float DepartmentChooseTarget = 30; //�t�w��׾Ǥ�
	private float FreeTarget = 20; //�ۥѿ�׾Ǥ�
	
	//�ϥΪ̷�e�Ǥ�
	private float CommonMust = 0; //�@�P���׾Ǥ�
	private float CommonLanguage = 0; //�@�P�y��q�ѾǤ�
	private float CollegeMust = 0; //�|���׾Ǥ�
	private float DepartmentMust = 0; //�t�w���׾Ǥ�
	private float DepartmentChoose = 0; //�t�w��׾Ǥ�
	private float Free = 0; //�ۥѿ�׾Ǥ�
	private float LiberalArt = 0;
	
	//���Ǵ��׽ҫ�֥[�o��Ǥ�
	private float TermCommonMust = 0; //�@�P���׾Ǥ�
	private float TermCommonLanguage = 0; //�@�P�y��q�ѾǤ�
	private float TermCommonChoose = 0; //�q�ѿ��
	private float TermCollegeMust = 0; //�|���׾Ǥ�
	private float TermDepartmentMust = 0; //�t�w���׾Ǥ�
	private float TermDepartmentChoose = 0; //�t�w��׾Ǥ�
	private float TermFree = 0; //�ۥѿ�׾Ǥ�
	private float TermLiberalArt = 0;
	
	public ClassDivider() //Constructor
	{
		// ���L�ȬO�ΨӧP�_�O�_���ӾǴ�����
		Must = new ClassArray("����", true); // ����
		Pro = new ClassArray("�M�~���", true); // �M�~���
		Ban = new ClassArray("���L", true); // �S�L <60��
		NotFit = new ClassArray("���ŦX�ۥѿ��", true); // ���ŦX�ۥѿ��
		Common = new ClassArray("�q��", true); // �q��
		ThisTerm = new ClassArray("���Ǵ��ҵ{", false); // ���b��
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
			printPointSituation();
			break;
		case 7:
			printNowPointSituation(); //�[�W��Ǵ��׽ҫᵲ�G
			break;
		}
	}

	public void decode(FileReader term_info, int y, int t) throws Exception // ��Ѻ�����r
	{
		BufferedReader br = new BufferedReader(term_info);
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
				if (Split_test[0].equals("�Ǵ����Z"))
					IsGetAllGrade = true;
				else
				{
					
					for(int j =0; j < Split_test.length;j++) 
					{
						System.out.print(Split_test[j]+" ");
						
						if(j == (Split_test.length-1)) 
						{
							System.out.print("\n");
						}
					}
					
					if (Split_test.length == 6)
					{
						// �w�׹L(���Ƥw�g�X�ӡ^�A���]���ҵ{�W�٤����ť���ӳQ��Ѧh�@�Ӧr��
						ClassInfo CI = new ClassInfo(y, t, Split_test[0], Split_test[1] + Split_test[2],
								(short) ((Split_test[3].equals("����")) ? 1 : 0), Float.parseFloat(Split_test[4]),
								Float.parseFloat(Split_test[5]));
						classify(CI);
					} 
					else
					{
						if (Split_test[2].equals("����") || Split_test[2].equals("���"))
						{
							if (Split_test.length > 4) // �w�׹L(���Ƥw�g�X��
							{
								if(Split_test[4].equals("�q�L"))
								{
									ClassInfo CI = new ClassInfo(y, t, Split_test[0], Split_test[1],
											(short) ((Split_test[2].equals("����")) ? 1 : 0), Float.parseFloat(Split_test[3]),
											80);
									classify(CI);
								}
								else if(Split_test[4].equals("���q�L"))
								{
									ClassInfo CI = new ClassInfo(y, t, Split_test[0], Split_test[1],
											(short) ((Split_test[2].equals("����")) ? 1 : 0), Float.parseFloat(Split_test[3]),
											0);
									classify(CI);
								}
								else
								{
									ClassInfo CI = new ClassInfo(y, t, Split_test[0], Split_test[1],
											(short) ((Split_test[2].equals("����")) ? 1 : 0), Float.parseFloat(Split_test[3]),
											Float.parseFloat(Split_test[4]));
									classify(CI);
								}
							} 
							else
							{// ��Ǵ�(���ƥ��X��
								ClassInfo CI = new ClassInfo(y, t, Split_test[0], Split_test[1],
										(short) ((Split_test[2].equals("����")) ? 1 : 0), Float.parseFloat(Split_test[3]),
										-1);
								classify(CI);
							}
						} 
						else
						{ // ��Ǵ�(���ƥ��X�ӡ^�A���]���ҵ{�W�٤����ť���ӳQ��Ѧh�@�Ӧr��
							ClassInfo CI = new ClassInfo(y, t, Split_test[0], Split_test[1] + Split_test[2],
									(short) ((Split_test[3].equals("����")) ? 1 : 0), Float.parseFloat(Split_test[4]),
									-1);
							classify(CI);
						}
					}
				}
			}
	}

	private void classify(ClassInfo CI) throws Exception// �����ǤJ�}�C
	{
		// Ū���t�W�ӻ{���ۥѿ��
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

		// �}�l�P�_
		if (CI.getScore() >= 60)
		{
			if (CI.getNecessary() == 1)// ����
			{
				String N = CI.getSerialNum().substring(0, 3);
				if (N.equals("ACS") || N.equals("ZCS") || N.equals("AGE"))
				{
					if(N.equals("ACS"))  //�ˬd�O�_���t�W����
					{
						DepartmentMust += CI.getPoint();
					}
					else if(N.equals("ZCS"))  //�ˬd�O�_���|����
					{
						CollegeMust += CI.getPoint(); 
					}
					else if(N.equals("AGE")) 
					{
						if(CI.getClassName().length() >= 4)
						{
							String CN =  CI.getClassName().substring(0,4);
							if(CN.equals("�j�@��|") || CN.equals("�j�G��|")) 
							{
								//�ˬd�O�_����|��
								CommonMust += CI.getPoint();
							}
							if(CN.equals("�ն�����")) 
							{
								LiberalArt += CI.getPoint();
							}
						}
						if(CI.getClassName().length() >= 2) 
						{
							String CN = CI.getClassName().substring(0,2);
							if(CN.equals("���") || CN.equals("�^��"))
							{
								//�ˬd�O�_���y���
								CommonLanguage += CI.getPoint(); 
							}
						}
					}
					Must.add(CI);
				}
				else
					NotFit.add(CI);
			} 
			else// ���
			{
				if (CI.getSerialNum().substring(0, 3).equals("ACS"))
				{
					if(DepartmentChoose < DepartmentChooseTarget) //�t�W��׭Y�W�L���e�A�[��ۥѿ�׾Ǥ�
					{
						DepartmentChoose += CI.getPoint();
						if(DepartmentChoose > DepartmentChooseTarget) //�[���Ǥ���A�Y�W�L�t�W��ת��e,�h�l�����ܦۥѿ��
						{
							Free += DepartmentChoose - DepartmentMustTarget;
							DepartmentChoose = DepartmentMustTarget;
						}
						
					}else
					{
						Free += CI.getPoint();
					}
					Pro.add(CI);
				}
				else if (CI.getSerialNum().substring(0, 3).equals("AGE")) //�ĭp�Ǯճq�ѭ������w������
				{
					Common.add(CI);
					if(CI.getClassName().length() >= 4)
					{
						String CN =  CI.getClassName().substring(0,4);
						if(CN.equals("�ն�����")) 
						{
							LiberalArt += CI.getPoint();
						}
					}
				}
				else
				{
					boolean IsFind = false;
					for (int i = 0; i < FreeList.size(); i++) 
					{
						if (FreeList.get(i).equals(CI.getClassName()))// �ŦX�ۥѿ��
						{
							Pro.add(CI);
							Free += CI.getPoint();
							IsFind = true;
						}
					}
					if(!IsFind) 
					{
						NotFit.add(CI);// ���ŦX�ۥѿ��
					}
				}
			}
		} 
		else
		{
			if (CI.getScore() == -1)// ���b��
			{
				if (CI.getNecessary() == 1)// ����
				{
					String N = CI.getSerialNum().substring(0, 3);
					if (N.equals("ACS") || N.equals("ZCS") || N.equals("AGE"))
					{
						if(N.equals("ACS"))  //�ˬd�O�_���t�W����
						{
							TermDepartmentMust += CI.getPoint();
						}
						else if(N.equals("ZCS"))  //�ˬd�O�_���|����
						{
							TermCollegeMust += CI.getPoint(); 
						}
						else if(N.equals("AGE")) 
						{
							if(CI.getClassName().length() >= 4)
							{
								String CN =  CI.getClassName().substring(0,4);
								if(CN.equals("�j�@��|") || CN.equals("�j�G��|")) 
								{
									//�ˬd�O�_����|��
									TermCommonMust += CI.getPoint();
								}
							}
							if(CI.getClassName().length() >= 2) 
							{
								String CN = CI.getClassName().substring(0,2);
								if(CN.equals("���") || CN.equals("�^��"))
								{
									//�ˬd�O�_���y���
									TermCommonLanguage += CI.getPoint(); 
								}
							}
						}
					}
				}
				else// ���
				{
					if (CI.getSerialNum().substring(0, 3).equals("ACS"))
					{
						TermDepartmentChoose += CI.getPoint();
					}
					else if (CI.getSerialNum().substring(0, 3).equals("AGE")) //�ĭp�Ǯճq�ѭ������w������
					{
						//�ݭn�P�O�֤߫D�֤�
						TermCommonChoose += CI.getPoint();
						if(CI.getClassName().length() >= 4)
						{
							String CN =  CI.getClassName().substring(0,4);
							if(CN.equals("�ն�����")) 
							{
								TermLiberalArt += CI.getPoint();
							}
						}
					}
					else
					{
						for (int i = 0; i < FreeList.size(); i++) 
						{
							if (FreeList.get(i).equals(CI.getClassName()))// �ŦX�ۥѿ��
							{
								TermFree += CI.getPoint();
							}
						}
					}
				}
				ThisTerm.add(CI);
			}
			else
				Ban.add(CI);// ���ή�S�׹L
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
		
		System.out.print("�z�ثe�֥i���Ǥ��`�Ǥ����G " + 
				(CommonMust+CommonLanguage+CollegeMust+DepartmentMust+DepartmentChoose+Free+LiberalArt)
		+ "�Ǥ�\n\n");
		System.out.print("�t�W�׽ұ��ΡG\n");
		if(CollegeMust < CollegeMustTarget) {
			System.out.print("�|�����׾Ǥ����G "+ CollegeMust + "�Ǥ� (���q�w�ײ��Ǥ�"+CollegeMustTarget+"�Ǥ��A�ٯʤ�"+(DepartmentMustTarget-CollegeMust)+"�Ǥ�)\n");
		}else 
		{
			System.out.print("�|�����׾Ǥ����G "+ CollegeMust + "�Ǥ� (�q�w�ײ��Ǥ�"+CollegeMustTarget+"�Ǥ��A���߹F���ؼ�!)\n");
		}
		if(DepartmentMust < DepartmentMustTarget) {
			System.out.print("�t�W���׾Ǥ����G "+ DepartmentMust + "�Ǥ� (���q�w�ײ��Ǥ�"+DepartmentMustTarget+"�Ǥ��A�ٯʤ�"+(DepartmentMustTarget-DepartmentMust)+"�Ǥ�)\n");
		}else 
		{
			System.out.print("�t�W���׾Ǥ����G "+ DepartmentMust + "�Ǥ� (�q�w�ײ��Ǥ�"+DepartmentMustTarget+"�Ǥ� �A���߹F���ؼ�!)\n");
		}
		if(DepartmentChoose < DepartmentChooseTarget) {
			System.out.print("�t�W�M�~��׾Ǥ����G "+ DepartmentChoose + "�Ǥ� (���q�w�ײ��Ǥ�"+DepartmentChooseTarget+"�Ǥ��A�ٯʤ�"+(DepartmentChooseTarget-DepartmentChoose)+"�Ǥ�)\n");
		}else 
		{
			System.out.print("�t�W�M�~��׾Ǥ����G "+ DepartmentChoose + "�Ǥ� (�q�w�ײ��Ǥ�"+DepartmentChooseTarget+"�Ǥ��A ���߹F���ؼ�!)\n");
		}
		if(Free < FreeTarget) {
			System.out.print("�ۥѾǤ����G "+ Free + "�Ǥ� (���q�w�ײ��Ǥ�"+FreeTarget+"�Ǥ��A�ٯʤ�"+(FreeTarget-Free)+"�Ǥ�)\n");
		}else 
		{
			System.out.print("�ۥѾǤ����G "+ Free + "�Ǥ� (�q�w�ײ��Ǥ�"+FreeTarget+"�Ǥ��A���߹F���ؼ�!)\n");
		}
		
		System.out.print("\n�q�ѭ׽ұ��ΡG\n");
		if(CommonMust < CommonMustTarget) {
			System.out.print("�@�P���׾Ǥ����G "+ CommonMust + "�Ǥ� (���q�w�ײ��Ǥ�"+CommonMustTarget+"�Ǥ��A�ٯʤ�"+(CommonMustTarget-CommonMust)+"�Ǥ�)\n");
		}else 
		{
			System.out.print("�@�P���׾Ǥ����G "+ CommonMust + "�Ǥ� (�q�w�ײ��Ǥ�"+CommonMustTarget+"�Ǥ��A ���߹F���ؼ�!)\n");
		}
		
		if(CommonLanguage < CommonLanguageTarget) {
			System.out.print("�@�P�y��q�ѾǤ����G "+ CommonLanguage + "�Ǥ� (���q�w�ײ��Ǥ�"+CommonLanguage+"�Ǥ��A�ٯʤ�"+(CommonLanguageTarget-CommonLanguage)+"�Ǥ�)\n");
		}else 
		{
			System.out.print("�@�P�y��q�ѾǤ����G "+ CommonLanguage + "�Ǥ� (�q�w�ײ��Ǥ�"+CommonLanguageTarget+"�Ǥ��A���߹F���ؼ�!)\n");
		}
		System.out.print("�ն�����Ǥ����G "+LiberalArt+"�Ǥ�(�`�N�G�ն�����|���Q�O�J�q�ѿ��,�Цۦ�p��C)\n");
	}
	
	public void printNowPointSituation() 
	{
		System.out.print("�z�ثe�֥i���Ǥ��`�Ǥ����G " + 
				(CommonMust+CommonLanguage+CollegeMust+DepartmentMust+DepartmentChoose+Free+LiberalArt
						+TermCommonMust+TermCommonLanguage+TermCollegeMust+TermDepartmentMust+TermDepartmentChoose+TermFree+TermCommonChoose+TermLiberalArt)
		+ "�Ǥ�\n\n");
		
		float tmp_CommonMust = CommonMust + TermCommonMust;
		float tmp_CommonLanguage = CommonLanguage + TermCommonLanguage;
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
		
		System.out.print("�t�W�׽ұ��ΡG\n");
		if(tmp_CollegeMust < CollegeMustTarget) {
			System.out.print("�|�����׾Ǥ����G "+ tmp_CollegeMust + "�Ǥ� (���q�w�ײ��Ǥ�"+CollegeMustTarget+"�Ǥ��A�ٯʤ�"+(DepartmentMustTarget-tmp_CollegeMust)+"�Ǥ�)\n");
		}else 
		{
			System.out.print("�|�����׾Ǥ����G "+ tmp_CollegeMust + "�Ǥ� (�q�w�ײ��Ǥ�"+CollegeMustTarget+"�Ǥ��A���߹F���ؼ�!)\n");
		}
		if(tmp_DepartmentMust < DepartmentMustTarget) {
			System.out.print("�t�W���׾Ǥ����G "+ tmp_DepartmentMust + "�Ǥ� (���q�w�ײ��Ǥ�"+DepartmentMustTarget+"�Ǥ��A�ٯʤ�"+(DepartmentMustTarget-tmp_DepartmentMust)+"�Ǥ�)\n");
		}else 
		{
			System.out.print("�t�W���׾Ǥ����G "+ tmp_DepartmentMust + "�Ǥ� (�q�w�ײ��Ǥ�"+DepartmentMustTarget+"�Ǥ� �A���߹F���ؼ�!)\n");
		}
		if(tmp_DepartmentChoose < DepartmentChooseTarget) {
			System.out.print("�t�W�M�~��׾Ǥ����G "+ tmp_DepartmentChoose + "�Ǥ� (���q�w�ײ��Ǥ�"+DepartmentChooseTarget+"�Ǥ��A�ٯʤ�"+(DepartmentChooseTarget-tmp_DepartmentChoose)+"�Ǥ�)\n");
		}else 
		{
			System.out.print("�t�W�M�~��׾Ǥ����G "+ tmp_DepartmentChoose + "�Ǥ� (�q�w�ײ��Ǥ�"+DepartmentChooseTarget+"�Ǥ��A ���߹F���ؼ�!)\n");
		}
		
		if(tmp_Free < FreeTarget) {
			System.out.print("�ۥѾǤ����G "+ tmp_Free + "�Ǥ� (���q�w�ײ��Ǥ�"+FreeTarget+"�Ǥ��A�ٯʤ�"+(FreeTarget-tmp_Free)+"�Ǥ�)\n");
		}else 
		{
			System.out.print("�ۥѾǤ����G "+ tmp_Free + "�Ǥ� (�q�w�ײ��Ǥ�"+FreeTarget+"�Ǥ��A���߹F���ؼ�!)\n");
		}
	}
	
}
