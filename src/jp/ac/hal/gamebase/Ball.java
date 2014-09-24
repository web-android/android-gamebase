package jp.ac.hal.gamebase;

import java.util.Random;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;


public class Ball {
	private static String m_selected = "";
	public static Selected m_selected_one;
	public class Selected{
	    public synchronized String getSelected(){
	        return m_selected;
	    }
	    public synchronized void setSelected(String bar){
	        m_selected = bar;
	    }
	}
	
	public static MainActivity m_activity;
    private static final String TAG = "RemoteActivity";
	
	public Ball(SampleSurfaceView owner) {
		this(owner, -1, -1);
	}
	public Ball(SampleSurfaceView owner, double x, double y) {
		if(m_selected_one == null)m_selected_one = new Selected();
		Random r = new Random();
		if(x >= 0 && y >= 0){
			m_x = x;
			m_y = y;
		}
		else{
			if(owner.m_width != 0){
				m_x = r.nextInt(owner.m_width); // ### ここがゼロになることがある
				m_y = r.nextInt(owner.m_height);
			}
			else{
				m_x = 100;
				m_y = 100;
			}
		}
		double speed = 5 + r.nextInt(20);
		double rad = r.nextInt(360) / 360.0f * Math.PI * 2;
		m_mx = speed * Math.cos(rad);
		m_my = speed * Math.sin(rad);
		m_r = 25 + m_rand.nextInt(50);
		
		// デフォルト色設定
		m_values[0] = 200; //a
		m_values[1] = 359; //h 色相
		m_values[2] = 1; //s 彩度
		m_values[3] = 1; //v 明度
		
		// 色相
		setRandomColor();
	}
	public void onSeekChanged(float[] values){
		float h = m_values[1];
		m_values = values.clone();
		m_values[1] = h;
		m_color = Color.HSVToColor((int)m_values[0], new float[]{m_values[1], m_values[2], m_values[3]});
	}
	public void setRandomColor(){
		// アルファ (0-255)
		//m_values[0] = 200;
		// 色相 (0 - 359)
		m_values[1] = m_rand.nextInt(360);
		// 彩度 (0 - 1)
		//m_values[2] = 1.0f;
		// 明度 (0 - 1)
		//m_values[3] = 0.5f;
		// 色
		m_color = Color.HSVToColor((int)m_values[0], new float[]{m_values[1], m_values[2], m_values[3]});
	}

	public void frame(SampleSurfaceView owner, float[] accs) {
		boolean bound = false;
		// 加速度（横向きの場合）
		m_my +=  accs[1] * 0.1 * 2 * 0.3; // -1 - +1
		m_mx += -accs[0] * 0.1 * 2 * 0.3; // -2 - +2
		// 速度調整
		/*
		if(Math.abs(m_mx) > 10){
			m_mx = 10 * Math.signum(m_mx);
		}
		if(Math.abs(m_my) > 10){
			m_my = 10 * Math.signum(m_my);
		}
		*/
		// 加速度（縦向きの場合）
		// m_my += -accs[0] * 0.1 * 2; // -1 - +1
		// m_mx += -accs[1] * 0.1 * 2; // -2 - +2
		// 移動
		m_x += m_mx;
		m_y += m_my;
		// 跳ね返り処理
		if (m_x < 0){
			m_x = 0;
			m_mx = Math.abs(m_mx) * 0.9f;
			bound = true;
		}
		else if(m_x > owner.m_width){
			m_x = owner.m_width;
			m_mx = -Math.abs(m_mx) * 0.9f;
			bound = true;
		}
		if (m_y < 0){
			m_y = 0;
			m_my = Math.abs(m_my) * 0.9f;
			bound = true;
		}
		else if(m_y > owner.m_height){
			m_y = owner.m_height;
			m_my = -Math.abs(m_my) * 0.9f;
			bound = true;
		}
		// 跳ね返り音
		if(bound && false){
			int r = m_rand.nextInt(10);
			r = 4;
			float rate = 0.5f + m_rand.nextFloat() * 0.2f;
			owner.m_soundPool.play(owner.m_soundIds[r], 1.0f, 1.0f, 0, 0, rate);
		}
	}

	public void draw(Canvas canvas) {
		Paint paint = new Paint();
		//paint.setColor(Color.WHITE);
		// 色相、明度、彩度
		paint.setColor(m_color);
		//paint.setAlpha(100);
		canvas.drawCircle((float) m_x, (float) m_y, (float)m_r, paint);
	}

	private float[] m_values = new float[4];
	private Random m_rand = new Random();
	public double m_x = 0;
	public double m_y = 0;
	public double m_r = 50; // 半径
	public double m_mx = 10;
	public double m_my = 10;
	private int m_color = 0; // Color
	
}