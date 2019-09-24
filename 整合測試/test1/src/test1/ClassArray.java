package test1;
import java.util.ArrayList;
public class ClassArray
{
	private String name;
	private ArrayList<ClassInfo> class_array;
	private boolean InTerm;

	public ClassArray()
	{
		name = "";
		class_array = new ArrayList<ClassInfo>();
		InTerm = true;
	}

	public ClassArray(String st, boolean inTerm)
	{
		name = st;
		class_array = new ArrayList<ClassInfo>();
		InTerm = inTerm;
	}

	public void add(ClassInfo CI)
	{
		class_array.add(CI);
	}

	public int getLength()
	{
		return class_array.size();
	}

	public float getTotalPoint()// ���o�䤺�ҵ{�Ҧ��Ǥ�
	{ 
		float tmp_sum_point = 0;
		for (ClassInfo CI : class_array)
		{
			tmp_sum_point += CI.getPoint();
		}
		return tmp_sum_point;
	}

	public void printTotalPoint()// �L�X�䤺�Ҧ��Ǥ�
	{ 
		float tmp_sum_point = 0;
		for (ClassInfo CI : class_array)
		{
			tmp_sum_point += CI.getPoint();
		}
		System.out.print("�`�Ǥ����G " + tmp_sum_point);
	}

	public void print()// �L�X�Ҧ��ҵ{
	{ 
		if (getLength() > 0)
		{
			if (InTerm)
			{
				System.out.print("\n" + name + "\n");
				System.out.print("-------------------------------------------------\n");
				System.out.print("|�ҵ{�N��|��/���|�Ǥ�|���Z|        �ҵ{�W��           |\n");
				System.out.print("-------------------------------------------------\n");
				for (ClassInfo CI : class_array)
				{
					System.out.print("|" + CI.getSerialNum());
					System.out.printf("|%7d| %-1.1f|%-2.1f|", CI.getNecessary(), CI.getPoint(), CI.getScore());
					System.out.printf("%12s", CI.getClassName());
					System.out.print("\n-------------------------------------------------\n");
				}
			} 
			else
			{
				System.out.print("\n" + name + "\n");
				System.out.print("--------------------------------------------\n");
				System.out.print("|�ҵ{�N��|��/���|�Ǥ�|        �ҵ{�W��           |\n");
				System.out.print("--------------------------------------------\n");
				for (ClassInfo CI : class_array)
				{
					System.out.print("|" + CI.getSerialNum());
					System.out.printf("|%7d| %-1.1f|", CI.getNecessary(), CI.getPoint());
					System.out.printf("%12s", CI.getClassName());
					System.out.print("\n--------------------------------------------\n");
				}
			}
		} 
		else
		{
			System.out.print("\n" + name + "\n");
			System.out.print("-----------------------�L------------------------\n");
		}
	}

}
