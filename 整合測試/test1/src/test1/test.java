package test1;

public class test
{
	public static void main(String[] args) throws Exception
	{
		ClassDivider cd = new ClassDivider();
		
		int year = 106;                      // �Ǧ~
		int term = 1;                        // �Ǵ� (1�W�Ǵ� 2�U�Ǵ�)
		String serial_num = "ACS";           // �ҵ{�N�� EX.ACS
		String class_name = "���սҵ{";      // �ҵ{�W�� (����)
		short necessary = 1;                 // ���� = 1 ,��� = 0
		float point = 3;                     // �Ǥ�
		float score = 99;                    // ����, ���b�ץ����o���Ƥ��ҵ{ = -1
		
		ClassInfo testClass = new ClassInfo(year,term,serial_num,class_name,necessary,point,score);
		cd.classify(testClass);
		cd.print();
	}
	
}
