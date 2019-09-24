package test1;

public class ClassInfo
{
	private int year;
	private int term;
	private String serial_num; // �ҵ{�N��
	private String class_name; // �ҵ{�W��
	private short necessary; // ���� = 1 ,��� = 0
	private float point; // �Ǥ�
	private float score; // ����, �����o���� = -1

	public ClassInfo(int y, int t, String sn, String cn, short n, float p, float s)
	{
		year = y;
		term = t;
		serial_num = sn;
		class_name = cn;
		necessary = n;
		point = p;
		score = s;
	}

	public int getYear()
	{
		return year;
	}

	public int getTerm()
	{
		return term;
	}

	public String getSerialNum()
	{
		return serial_num;
	}

	public String getClassName()
	{
		return class_name;
	}

	public short getNecessary()
	{
		return necessary;
	}

	public float getPoint()
	{
		return point;
	}

	public float getScore()
	{
		return score;
	}
}
