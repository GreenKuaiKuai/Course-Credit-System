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

	public float getTotalPoint()// 取得其內課程所有學分
	{ 
		float tmp_sum_point = 0;
		for (ClassInfo CI : class_array)
		{
			tmp_sum_point += CI.getPoint();
		}
		return tmp_sum_point;
	}

	public void printTotalPoint()// 印出其內所有學分
	{ 
		float tmp_sum_point = 0;
		for (ClassInfo CI : class_array)
		{
			tmp_sum_point += CI.getPoint();
		}
		System.out.print("總學分為： " + tmp_sum_point);
	}

	public void print()// 印出所有課程
	{ 
		if (getLength() > 0)
		{
			if (InTerm)
			{
				System.out.print("\n" + name + "\n");
				System.out.print("-------------------------------------------------\n");
				System.out.print("|課程代號|必/選修|學分|成績|        課程名稱           |\n");
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
				System.out.print("|課程代號|必/選修|學分|        課程名稱           |\n");
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
			System.out.print("-----------------------無------------------------\n");
		}
	}

}
