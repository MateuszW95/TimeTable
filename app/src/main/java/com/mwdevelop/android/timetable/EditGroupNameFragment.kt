package com.mwdevelop.android.timetable

import android.app.Dialog
import android.app.DialogFragment
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText

/**
 * Created by mateusz on 08.03.18.
 */
class EditGroupNameFragment: DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val v:View= LayoutInflater.from(activity).inflate(R.layout.dialogedit,null)
        var name:String=arguments.getString(ARG_NAME)
        var ET_name:EditText=v.findViewById(R.id.et_name)
        ET_name.text=Editable.Factory.getInstance().newEditable(name)
        return AlertDialog.Builder(activity,R.style.MyDialogTheme).setTitle(R.string.editGroup_name).
                setView(v).
                setPositiveButton(android.R.string.ok,null).
                setNegativeButton(android.R.string.cancel,null).

                create()
    }

    companion object {
        private val ARG_NAME="NAME_GROUP"

        fun newInstance(name:String):EditGroupNameFragment{
            var args=Bundle()
            args.putString(ARG_NAME,name)

            var fragment=EditGroupNameFragment()
            fragment.arguments=args
            return fragment

        }
    }
}