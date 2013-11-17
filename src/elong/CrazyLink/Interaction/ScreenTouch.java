/**********************************************************
 * ��Ŀ���ƣ�ɽկ������������Ϸ7�ս̳�
 * ��          �ߣ�֣����
 * ��Ѷ΢����SuperCube3D
 * ��          �ڣ�2013��10��
 * ��          ������Ȩ����   ��Ȩ�ؾ�
 * ��Դ���빩�����о�ѧϰOpenGL ES����AndroidӦ���ã�
 * ����ȫ���򲿷�������ҵ��;
 ********************************************************/

package elong.CrazyLink.Interaction;

import elong.CrazyLink.CrazyLinkConstent;
import elong.CrazyLink.Core.ControlCenter;
import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.view.MotionEvent;

public class ScreenTouch {
	
	Context mContext;
	
	float mPreviousX = 0.0f;
	float mPreviousY = 0.0f;
	
	int mWidth = 0;
	int mHeight = 0;
	int mGridX = 0;
	int mGridY = 0;
	int mStep = 0;
	int mYStart = 0;

	enum TOUCH_DIRECTION
	{
		UP,		
		DOWN,
		LEFT,
		RIGHT,
		INVALID		//������Ч
	}

	
	TOUCH_DIRECTION mDirection = TOUCH_DIRECTION.INVALID;
	
	public ScreenTouch(Context context, int width, int height)
	{
		mContext = context;
		mWidth = width;
		mHeight = height;
		mStep = (int) (width / CrazyLinkConstent.GRID_NUM);
		mYStart = (mHeight - mWidth) / 2;
	}
	
	//��������
	public boolean Touch(MotionEvent e) {
		float y = e.getY();
		float x = e.getX();
		switch (e.getAction()) {
			case MotionEvent.ACTION_DOWN:
				mGridX = -1;
				mGridY = -1;
				mDirection = TOUCH_DIRECTION.INVALID;
				if (x >= 0 && x < mStep) mGridX = 0;
				else if(x >= mStep && x < 2*mStep) mGridX = 1;
				else if(x >= 2*mStep && x < 3*mStep) mGridX = 2;
				else if(x >= 3*mStep && x < 4*mStep) mGridX = 3;
				else if(x >= 4*mStep && x < 5*mStep) mGridX = 4;
				else if(x >= 5*mStep && x < 6*mStep) mGridX = 5;
				else if(x >= 6*mStep && x < 7*mStep) mGridX = 6;
				
				if(y >= mYStart && y < mYStart + mStep) mGridY = 6;
				else if(y >= mYStart + mStep && y < mYStart + 2*mStep) mGridY = 5;
				else if(y >= mYStart + 2*mStep && y < mYStart + 3*mStep) mGridY = 4;
				else if(y >= mYStart + 3*mStep && y < mYStart + 4*mStep) mGridY = 3;
				else if(y >= mYStart + 4*mStep && y < mYStart + 5*mStep) mGridY = 2;
				else if(y >= mYStart + 5*mStep && y < mYStart + 6*mStep) mGridY = 1;
				else if(y >= mYStart + 6*mStep && y < mYStart + 7*mStep) mGridY = 0;
				break;
			case MotionEvent.ACTION_UP:
				RaiseTouchEvent();
				break;
			case MotionEvent.ACTION_MOVE:
			    float dy = y - mPreviousY;//���㴥�ر�Yλ��
			    float dx = x - mPreviousX;//���㴥�ر�Yλ��
			    if(Math.abs(dy) > Math.abs(dx))
			    {
			    	if(dy > 0) mDirection = TOUCH_DIRECTION.DOWN;
			    	else if(dy < 0) mDirection = TOUCH_DIRECTION.UP;
			    }
			    else
			    {
			    	if(dx > 0) mDirection = TOUCH_DIRECTION.RIGHT;
			    	else if(dx < 0) mDirection = TOUCH_DIRECTION.LEFT; 	
			    }
		}
		mPreviousY = y;//��¼���ر�λ��
		mPreviousX = x;//��¼���ر�λ��
		return true;
	}	
	
	//��ȡ�����ĸ���
	int getGridX()
	{
		return mGridX;
	}
	
	//��ȡ�����ĸ���
	int getGridY()
	{
		return mGridY;
	}

	TOUCH_DIRECTION getDirection()
	{
		return mDirection;
	}
	
	//���ݷ�����Ϣ��ȡ��Ӧ���ھ�
	int getNeighborX()
	{
		int neighborX = mGridX;
		if (mDirection == TOUCH_DIRECTION.LEFT) neighborX--;
		else if(mDirection == TOUCH_DIRECTION.RIGHT) neighborX++;
		return neighborX;
	}

	//���ݷ�����Ϣ��ȡ��Ӧ���ھ�
	int getNeighborY()
	{
		int neighborY = mGridY;
		if (mDirection == TOUCH_DIRECTION.DOWN) neighborY--;
		else if(mDirection == TOUCH_DIRECTION.UP) neighborY++;
		return neighborY;
	}
	
	boolean isValidTouch()
	{		
		//У�鴥�������Ƿ���Ч
		if(-1 == mGridX || -1 == mGridY) return false;			
		if((0 == mGridX && mDirection == TOUCH_DIRECTION.LEFT)
				|| (6 == mGridX && mDirection == TOUCH_DIRECTION.RIGHT)
				|| (0 == mGridY && mDirection == TOUCH_DIRECTION.DOWN)
				|| (6 == mGridY && mDirection == TOUCH_DIRECTION.UP)
				|| mDirection == TOUCH_DIRECTION.INVALID)
		{
			return false;
		}
		return true;
	}

	//������Ч�Ĵ����¼�������Ϣ��mHandlerͳһ����
	void RaiseTouchEvent()
	{
		if(!isValidTouch())	//У�鶯���Ƿ�Ϸ�
			return;
		//Toast.makeText(mContext, "Direction:" + getDirection() + " (" + getGridX() + " ," + getGridY() + ")",
		//	     Toast.LENGTH_SHORT).show();
		Bundle b = new Bundle();
		b.putInt("col1", getGridX());
		b.putInt("row1", getGridY());
		b.putInt("col2", getNeighborX());
		b.putInt("row2", getNeighborY());
		Message msg = new Message();
	    msg.what = ControlCenter.EXCHANGE_START;
		msg.setData(b);
	    ControlCenter.mHandler.sendMessage(msg);
	}
	
}