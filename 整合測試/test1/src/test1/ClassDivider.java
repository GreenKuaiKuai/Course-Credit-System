package test1;
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

	private int SHPoint_K; //���|�H����-�֤�
	private int SHPoint; //���|�H����-�D��
	private int MSPoint_K; //�Ʋz��޻��-�֤�
	private int MSPoint; //�Ʋz��޻��-�D��
	private int ASPoint_K; //���N���M���-�֤�
	private int ASPoint; //���N���M���-�D��
	private int TotalCommonPoint;
	
	
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
	
	public ClassDivider() //Constructory
	{
		// ���L�ȬO�ΨӧP�_�O�_���ӾǴ�����
		Must = new ClassArray("����", true); // ����
		Pro = new ClassArray("�M�~���", true); // �M�~���
		Ban = new ClassArray("���L", true); // �S�L <60��
		NotFit = new ClassArray("���ŦX�ۥѿ��", true); // ���ŦX�ۥѿ��
		Common = new ClassArray("�q��", true); // �q��
		ThisTerm = new ClassArray("���Ǵ��ҵ{", false); // ���b��
	}

	public void print()//�L�X�Ҧ���Ʈw���G
	{
		Must.print();
		Pro.print(); 
		Ban.print();  
		NotFit.print();
		Common.print();
		ThisTerm.print();
	}

	public void classify(ClassInfo A) throws Exception// �����ǤJ�}�C
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
		if (A.getScore() >= 60)
		{
			if (A.getNecessary() == 1)// ����
			{
				String N = A.getSerialNum().substring(0, 3);
				if (N.equals("ACS") || N.equals("ZCS") || N.equals("AGE"))
				{
					if(N.equals("ACS"))  //�ˬd�O�_���t�W����
					{
						DepartmentMust += A.getPoint();
					}
					else if(N.equals("ZCS"))  //�ˬd�O�_���|����
					{
						CollegeMust += A.getPoint(); 
					}
					else if(N.equals("AGE")) 
					{
						if(A.getClassName().length() >= 4)
						{
							String CN =  A.getClassName().substring(0,4);
							if(CN.equals("�j�@��|") || CN.equals("�j�G��|")) 
							{
								//�ˬd�O�_����|��
								CommonMust += A.getPoint();
							}
							if(CN.equals("�ն�����")) 
							{
								LiberalArt += A.getPoint();
							}
						}
						if(A.getClassName().length() >= 2) 
						{
							String CN = A.getClassName().substring(0,2);
							if(CN.equals("���") || CN.equals("�^��"))
							{
								//�ˬd�O�_���y���
								CommonLanguage += A.getPoint(); 
							}
						}
					}
					Must.add(A);
				}
				else
					NotFit.add(A);
			} 
			else// ���
			{
				if (A.getSerialNum().substring(0, 3).equals("ACS"))
				{
					if(DepartmentChoose < DepartmentChooseTarget) //�t�W��׭Y�W�L���e�A�[��ۥѿ�׾Ǥ�
					{
						DepartmentChoose += A.getPoint();
						if(DepartmentChoose > DepartmentChooseTarget) //�[���Ǥ���A�Y�W�L�t�W��ת��e,�h�l�����ܦۥѿ��
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
				else if (A.getSerialNum().substring(0, 3).equals("AGE")) //�ĭp�Ǯճq�ѭ������w������
				{
					Common.add(A);
					if(A.getClassName().length() >= 4)
					{
						String CN =  A.getClassName().substring(0,4);
						if(CN.equals("�ն�����")) 
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
						if (FreeList.get(i).equals(A.getClassName()))// �ŦX�ۥѿ��
						{
							Pro.add(A);
							Free += A.getPoint();
							IsFind = true;
						}
					}
					if(!IsFind) 
					{
						NotFit.add(A);// ���ŦX�ۥѿ��
					}
				}
			}
		} 
		else
		{
			if (A.getScore() == -1)// ���b��
			{
				if (A.getNecessary() == 1)// ����
				{
					String N = A.getSerialNum().substring(0, 3);
					if (N.equals("ACS") || N.equals("ZCS") || N.equals("AGE"))
					{
						if(N.equals("ACS"))  //�ˬd�O�_���t�W����
						{
							TermDepartmentMust += A.getPoint();
						}
						else if(N.equals("ZCS"))  //�ˬd�O�_���|����
						{
							TermCollegeMust += A.getPoint(); 
						}
						else if(N.equals("AGE")) 
						{
							if(A.getClassName().length() >= 4)
							{
								String CN =  A.getClassName().substring(0,4);
								if(CN.equals("�j�@��|") || CN.equals("�j�G��|")) 
								{
									//�ˬd�O�_����|��
									TermCommonMust += A.getPoint();
								}
							}
							if(A.getClassName().length() >= 2) 
							{
								String CN = A.getClassName().substring(0,2);
								if(CN.equals("���") || CN.equals("�^��"))
								{
									//�ˬd�O�_���y���
									TermCommonLanguage += A.getPoint(); 
								}
							}
						}
					}
				}
				else// ���
				{
					if (A.getSerialNum().substring(0, 3).equals("ACS"))
					{
						TermDepartmentChoose += A.getPoint();
					}
					else if (A.getSerialNum().substring(0, 3).equals("AGE")) //�ĭp�Ǯճq�ѭ������w������
					{
						//�ݭn�P�O�֤߫D�֤�
						TermCommonChoose += A.getPoint();
						if(A.getClassName().length() >= 4)
						{
							String CN =  A.getClassName().substring(0,4);
							if(CN.equals("�ն�����")) 
							{
								TermLiberalArt += A.getPoint();
							}
						}
					}
					else
					{
						for (int i = 0; i < FreeList.size(); i++) 
						{
							if (FreeList.get(i).equals(A.getClassName()))// �ŦX�ۥѿ��
							{
								TermFree += A.getPoint();
							}
						}
					}
				}
				ThisTerm.add(A);
			}
			else
				Ban.add(A);// ���ή�S�׹L
		}
	}

}
