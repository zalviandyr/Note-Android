package com.zukron.note.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.card.MaterialCardView
import com.zukron.note.R
import com.zukron.note.model.DefaultNote
import com.zukron.note.model.ListNote
import com.zukron.note.model.Note
import com.zukron.note.util.Utilities.formatToString

/**
 * Project name is Note
 * Created by Zukron Alviandy R on 9/4/2020
 * Contact me if any issues on zukronalviandy@gmail.com
 */
class NoteAdapter(private val context: Context, private val noteList: List<Note>) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var defaultNoteList: List<DefaultNote>? = null
    var listNoteList: List<ListNote>? = null
    private var onClickSelected: OnClickSelected? = null
    private var shiftDefaultNote = 0
    private var shiftListNote = 0

    fun setOnClickSelected(onClickSelected: OnClickSelected) {
        this.onClickSelected = onClickSelected
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(context)

        return if (viewType == Note.Type.defaultNote || viewType == Note.Type.longNote) {
            val view = layoutInflater.inflate(R.layout.snippet_item_default_note, parent, false)
            DefaultNoteHolder(view)
        } else {
            val view = layoutInflater.inflate(R.layout.snippet_item_list_note, parent, false)
            ListNoteHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val note = noteList[position]

        when (holder.itemViewType) {
            Note.Type.defaultNote, Note.Type.longNote -> {
                if (holder.itemViewType == Note.Type.longNote) {
                    val layoutParams = holder.itemView.layoutParams as StaggeredGridLayoutManager.LayoutParams
                    layoutParams.isFullSpan = true
                }

                val defaultNoteHolder = holder as DefaultNoteHolder
                val defaultNote = defaultNoteList?.get(shiftDefaultNote)

                if (defaultNote != null) {
                    defaultNoteHolder.materialCard.setCardBackgroundColor(
                            ContextCompat.getColor(context, note.color)
                    )
                    defaultNoteHolder.tvTitle.text = defaultNote.title
                    defaultNoteHolder.tvBody.text = defaultNote.body
                    defaultNoteHolder.tvModifiedDate.text = note.modifiedDate.formatToString()

                    defaultNoteHolder.materialCard.setOnClickListener {
                        onClickSelected?.onClickSelected(note)
                    }
                    shiftDefaultNote++
                }
            }

            Note.Type.listNote -> {
                val listNoteHolder = holder as ListNoteHolder
                val listNote = listNoteList?.get(shiftListNote)

                if (listNote != null) {
                    // to avoid if item more than 4
                    val checkBoxSize = listNote.checkList.size
                            .coerceAtMost(4)

                    for (i in 0 until checkBoxSize) {
                        listNoteHolder.checkBoxesList[i].visibility = View.VISIBLE
                        listNoteHolder.textViewsList[i].visibility = View.VISIBLE

                        listNoteHolder.checkBoxesList[i].isChecked = listNote.checkList[i]
                        listNoteHolder.textViewsList[i].text = listNote.itemList[i]
                    }

                    listNoteHolder.materialCard.setCardBackgroundColor(
                            ContextCompat.getColor(context, note.color)
                    )
                    listNoteHolder.tvTitle.text = listNote.title
                    listNoteHolder.tvModifiedDate.text = note.modifiedDate.formatToString()

                    listNoteHolder.materialCard.setOnClickListener {
                        onClickSelected?.onClickSelected(note)
                    }
                    shiftListNote++
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return noteList.size
    }

    override fun getItemViewType(position: Int): Int {
        val note = noteList[position]
        return note.type
    }

    class DefaultNoteHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val materialCard: MaterialCardView = itemView.findViewById(R.id.mcv_snippet)
        val tvTitle: TextView = itemView.findViewById(R.id.tv_title_snippet)
        val tvBody: TextView = itemView.findViewById(R.id.tv_body_snippet)
        val tvModifiedDate: TextView = itemView.findViewById(R.id.tv_modified_date_snippet)
    }

    class ListNoteHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val materialCard: MaterialCardView = itemView.findViewById(R.id.mcv_snippet)
        val tvTitle: TextView = itemView.findViewById(R.id.tv_title_snippet)
        val tvModifiedDate: TextView = itemView.findViewById(R.id.tv_modified_date_snippet)
        val checkBoxesList: MutableList<CheckBox> = mutableListOf()
        val textViewsList: MutableList<TextView> = mutableListOf()

        init {
            val cb1: CheckBox = itemView.findViewById(R.id.cb_snippet_item_1)
            val cb2: CheckBox = itemView.findViewById(R.id.cb_snippet_item_2)
            val cb3: CheckBox = itemView.findViewById(R.id.cb_snippet_item_3)
            val cb4: CheckBox = itemView.findViewById(R.id.cb_snippet_item_4)

            val tv1: TextView = itemView.findViewById(R.id.tv_snippet_item_1)
            val tv2: TextView = itemView.findViewById(R.id.tv_snippet_item_2)
            val tv3: TextView = itemView.findViewById(R.id.tv_snippet_item_3)
            val tv4: TextView = itemView.findViewById(R.id.tv_snippet_item_4)

            checkBoxesList.add(cb1)
            checkBoxesList.add(cb2)
            checkBoxesList.add(cb3)
            checkBoxesList.add(cb4)

            textViewsList.add(tv1)
            textViewsList.add(tv2)
            textViewsList.add(tv3)
            textViewsList.add(tv4)

            for (i in 0 until checkBoxesList.size) {
                checkBoxesList[i].visibility = View.GONE
                textViewsList[i].visibility = View.GONE
            }
        }
    }

    fun interface OnClickSelected {
        fun onClickSelected(note: Note)
    }
}