package com.mwdevelop.android.timetable

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.ViewPager
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_day_pager.*
import java.io.BufferedReader
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStreamReader

import java.util.*
import kotlin.collections.ArrayList

class DayPagerActivity : AppCompatActivity() {

    private lateinit var mViewPager: ViewPager
    private lateinit var mDays: ArrayList<Day>
    private val fileNameGroup="name"

    private lateinit var mDayNameTextView:TextView
    companion object {
        var context:Context?=null
    }

    lateinit var mDate:Date
    private var mCurrnetDay:Int?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_day_pager)
        context=this


        var mCalendar:Calendar= Calendar.getInstance()
        mCurrnetDay=mCalendar.get(Calendar.DAY_OF_WEEK)
        mViewPager=findViewById(R.id.day_view_pager)


        //Informatyka I-go st sem.4 gr. dziekańska 2 lab.4
        if(readNameGroup(fileNameGroup))
        {
            try {
                if(!object : DataDownloadTask() {}.execute(DataLab.get(applicationContext).mGroupName).get()) throw Exception("CONNECTION ERROR")
                LL_data.visibility = View.GONE
                mViewPager.visibility = View.VISIBLE
                mDays = DataLab.get(this).mDays!!
            }
            catch (e:Exception)
            {
                Toast.makeText(applicationContext,"Błąd pobierania danych", Toast.LENGTH_LONG).show()
                LL_data.visibility=View.VISIBLE
                mViewPager.visibility=View.GONE
            }
        }
        else
        {
            LL_data.visibility=View.VISIBLE
            mViewPager.visibility=View.GONE
        }
        bt_enter.setOnClickListener(View.OnClickListener {
            if(!ET_data.text.isEmpty())
            {
                DataLab.get(this).mGroupName=ET_data.text.toString()
                try{
                    if(!object : DataDownloadTask() {}.execute(DataLab.get(applicationContext).mGroupName).get()) throw Exception("CONNECTION ERROR")
                    writeNameGroup("name",this)
                    LL_data.visibility=View.GONE
                    mViewPager.visibility=View.VISIBLE
                    mDays= DataLab.get(this).mDays!!
                    mViewPager.adapter.notifyDataSetChanged()

                }
                catch(e:Exception){
                    Toast.makeText(this,"Błąd pobierania danych",Toast.LENGTH_LONG).show()
                }
            }
        })

        mDays= DataLab.get(this).mDays!!





        var fragmentManager:FragmentManager=supportFragmentManager

        mViewPager.adapter=object: FragmentStatePagerAdapter(fragmentManager){
            override fun getItem(position: Int): Fragment {
              var day:Day=mDays.get(position)
                return DayFragment.newInstance(day.id)
            }


            override fun getCount(): Int {
                return mDays.size
            }

        }
        //mViewPager.adapter.notifyDataSetChanged()
        if(mCurrnetDay!! ==0 || mCurrnetDay!! >6){
            mViewPager.currentItem = 0
        }
        else
        {
            mViewPager.currentItem = mCurrnetDay!!-1
        }

    }
    fun writeDataToFile(fileName:String,data:ArrayList<Day>){
       var gson=Gson()
        var data_str=gson.toJson(data)
        context!!.openFileOutput(fileName, Context.MODE_PRIVATE).use {
            it.write(data_str.toByteArray())
        }
    }

    fun writeNameGroup(fileName: String,context: Context){
        try {
            var fileOutputStream: FileOutputStream = openFileOutput(fileName, Context.MODE_PRIVATE)
            fileOutputStream.write(DataLab.get(context!!).mGroupName!!.toByteArray())
            fileOutputStream.close()
            Toast.makeText(applicationContext, "File saved", Toast.LENGTH_LONG).show()
        }
        catch (e:Exception){
            e.printStackTrace()
        }
    }

    fun readNameGroup(fileName: String):Boolean{
        var message:String?
        try {
            var fileInputStream: FileInputStream = openFileInput(fileName)
            var inputStreamReader = InputStreamReader(fileInputStream)
            var bufferedReader = BufferedReader(inputStreamReader)
            var stringBuffer = StringBuffer()
            message = bufferedReader.readLine()
            while (message != null) {
                stringBuffer.append(message)
                message = bufferedReader.readLine()
            }
            DataLab.get(applicationContext).mGroupName=stringBuffer.toString()
            return true
        }
        catch(e:Exception){
            return false
        }

    }
}
