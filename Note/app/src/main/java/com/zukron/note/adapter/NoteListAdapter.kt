package com.zukron.note.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.zukron.note.R
import com.zukron.note.util.Utilities.toEditable
import java.lang.IndexOutOfBoundsException

/**
 * Project name is Note
 * Created by Zukron Alviandy R on 9/4/2020
 * Contact me if any issues on zukronalviandy@gmail.com
 */

class NoteListAdapter(
        private val context: Context?,
        private val checkList: MutableList<Boolean>,
        private val itemList: MutableList<String>
) : RecyclerView.Adapter<NoteListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(context)
        val view = layoutInflater.inflate(R.layout.item_list_note, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val check = checkList[position]
        val item = itemList[position]

        holder.checkBox.isChecked = check
        holder.input.text = item.toEditable()
        holder.btnAdd.setOnClickListener {
            add()
        }
        holder.btnRemove.setOnClickListener {
            remove(position)
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    private fun add() {
        checkList.add(false)
        itemList.add("")

        val lastIndex = itemList.size - 1
        notifyItemInserted(lastIndex)
    }

    /***
     * mengatasi karena nilai posisi akan tetap menyimpan data yang tidak di re order
     */
    private fun remove(position: Int) {
        // jika element terakhir dihapus
        if (position == (itemList.size - 1)) {
            checkList.removeAt(position)
            itemList.removeAt(position)
            notifyItemRemoved(position)
        } else {
            var shift = 0
            while (true) {
                try {
                    checkList.removeAt(position - shift)
                    itemList.removeAt(position - shift)
                    notifyItemRemoved(position)
                    break
                } catch (e: IndexOutOfBoundsException) {
                    shift++
                }
            }
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val checkBox: CheckBox = itemView.findViewById(R.id.cb_item)
        val input: TextInputEditText = itemView.findViewById(R.id.input_item)
        val btnRemove: MaterialButton = itemView.findViewById(R.id.btn_remove_item)
        val btnAdd: MaterialButton = itemView.findViewById(R.id.btn_add_item)
    }

}