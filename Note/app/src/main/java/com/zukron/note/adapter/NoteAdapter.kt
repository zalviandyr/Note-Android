package com.zukron.note.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.card.MaterialCardView
import com.zukron.note.R
import com.zukron.note.model.DefaultNote
import com.zukron.note.model.ListNote
import com.zukron.note.model.MixNote
import com.zukron.note.model.Note

/**
 * Project name is Note
 * Created by Zukron Alviandy R on 9/4/2020
 * Contact me if any issues on zukronalviandy@gmail.com
 */
class NoteAdapter(
        private val context: Context
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var mixNote: MixNote? = null
        set(value) {
            noteList = value?.noteList
            defaultNoteList = value?.defaultNoteList
            listNoteList = value?.listNoteList

            shiftDefaultNote = 0
            shiftListNote = 0

            notifyDataSetChanged()

            field = value
        }

    private var noteList: List<Note>? = null
    private var defaultNoteList: List<DefaultNote>? = null
    private var listNoteList: List<List<ListNote>>? = null
    private var shiftDefaultNote: Int = 0
    private var shiftListNote: Int = 0
    private var onClickSelected: OnClickSelected? = null
    private val recyclerViewPool = RecyclerView.RecycledViewPool()

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
        val note = noteList?.get(position)

        if (note != null) {
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
                        defaultNoteHolder.tvModifiedDate.text = note.modifiedDate
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
                        listNoteHolder.tvTitle.text = listNote[0].title
                        listNoteHolder.tvModifiedDate.text = note.modifiedDate
                        listNoteHolder.materialCard.setCardBackgroundColor(
                                ContextCompat.getColor(context, note.color)
                        )

                        val itemNoteListAdapter = ItemNoteListAdapter(context, listNote)
                        holder.recyclerView.adapter = itemNoteListAdapter
                        holder.recyclerView.setRecycledViewPool(recyclerViewPool)
                        holder.materialCard.setOnClickListener {
                            onClickSelected?.onClickSelected(note)
                        }

                        shiftListNote++
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return noteList?.size ?: 0
    }

    override fun getItemViewType(position: Int): Int {
        val note = noteList?.get(position)
        return note?.type ?: 0
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
        val recyclerView: RecyclerView = itemView.findViewById(R.id.rv_snippet)
    }

    fun interface OnClickSelected {
        fun onClickSelected(note: Note)
    }
}