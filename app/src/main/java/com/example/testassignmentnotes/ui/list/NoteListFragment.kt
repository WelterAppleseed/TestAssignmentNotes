package com.example.testassignmentnotes.ui.list

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.testassignmentnotes.R
import com.example.testassignmentnotes.databinding.FragmentNoteListBinding
import com.example.testassignmentnotes.databinding.ListItemNoteBinding
import com.example.testassignmentnotes.di.DependencyManager
import com.example.testassignmentnotes.ui._base.FragmentNavigator
import com.example.testassignmentnotes.ui._base.OnNoteItemClick
import com.example.testassignmentnotes.ui._base.ViewBindingFragment
import com.google.android.material.snackbar.Snackbar
import com.example.testassignmentnotes.ui._base.entities.NoteListItem
import com.example.testassignmentnotes.ui._base.findImplementationOrThrow
import com.example.testassignmentnotes.ui.details.NoteDetailsFragment
import java.util.*

class NoteListFragment : ViewBindingFragment<FragmentNoteListBinding>(
    FragmentNoteListBinding::inflate
), OnNoteItemClick {
    private val noteViewModel: NoteViewModel by activityViewModels()
    private val notesAndNavigationViewModel by lazy { DependencyManager.noteListViewModel() }
    private val recyclerViewAdapter = RecyclerViewAdapter(this)
    override fun onViewBindingCreated(
        viewBinding: FragmentNoteListBinding,
        savedInstanceState: Bundle?
    ) {
        super.onViewBindingCreated(viewBinding, savedInstanceState)
        viewBinding.list.adapter = recyclerViewAdapter
        viewBinding.list.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                LinearLayout.VERTICAL
            )
        )
        viewBinding.createNoteButton.setOnClickListener {
            navigateTo(NoteDetailsFragment())
        }
        notesAndNavigationViewModel.getAllNotes().observe(
            viewLifecycleOwner
        ) {
            if (it != null) {
                recyclerViewAdapter.setItems(it)
                attachToRecycler(viewBinding.list, ArrayList(it))
            }
        }
        enableToolbarSorting(viewBinding.list)
        notesAndNavigationViewModel.navigateToNote.observe(
            viewLifecycleOwner
        ) {
           navigateTo(NoteDetailsFragment())
        }
    }

    override fun onNoteClick(note: NoteListItem) {
        noteViewModel.updateNote(note)
        navigateTo(NoteDetailsFragment())
    }

    private class RecyclerViewAdapter(noteClick: OnNoteItemClick) :
        RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {
        private val items = mutableListOf<NoteListItem>()
        private val noteC = noteClick
        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ) = ViewHolder(
            ListItemNoteBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

        override fun onBindViewHolder(
            holder: ViewHolder,
            position: Int
        ) {
            holder.bind(items[position])
        }

        override fun getItemCount() = items.size

        @SuppressLint("NotifyDataSetChanged")
        fun setItems(
            items: List<NoteListItem>
        ) {
            this.items.clear()
            this.items.addAll(items)
            notifyDataSetChanged()
        }

        fun addItem(item: NoteListItem, position: Int?) {
            this.items.add(position ?: this.items.size, item)
            notifyItemInserted(position ?: this.items.size)
        }

        fun removeItem(item: NoteListItem, position: Int?) {
            this.items.remove(item)
            notifyItemRemoved(position ?: this.items.size)
        }

        fun getItems(): List<NoteListItem> {
            return this.items
        }

        private inner class ViewHolder(
            private val binding: ListItemNoteBinding
        ) : RecyclerView.ViewHolder(
            binding.root
        ) {

            fun bind(
                note: NoteListItem
            ) {
                binding.noteView.setOnClickListener {
                    noteC.onNoteClick(note)
                }
                binding.titleLabel.text = note.title
                binding.contentLabel.text = note.content
            }

        }

    }

    @SuppressLint("NotifyDataSetChanged")
    private fun enableToolbarSorting(recycler: RecyclerView) {
        viewBinding?.toolbar?.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.modif_sort -> {
                    Collections.sort(
                        (recycler.adapter as RecyclerViewAdapter).getItems()
                    ) { o1, o2 ->
                        return@sort o2.modifiedAt!!.compareTo(o1.modifiedAt)

                    }
                    recycler.adapter?.notifyDataSetChanged()
                }
                R.id.def_sort -> {
                    Collections.sort(
                        (recycler.adapter as RecyclerViewAdapter).getItems()
                    ) { o1, o2 ->
                        return@sort o2.createdAt!!.compareTo(o1.createdAt)

                    }
                    recycler.adapter?.notifyDataSetChanged()
                }
            }
            return@setOnMenuItemClickListener true
        }
    }

    private fun attachToRecycler(recycler: RecyclerView, notes: ArrayList<NoteListItem>) {
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val deletedNote: NoteListItem =
                    notes[viewHolder.adapterPosition]
                val position = viewHolder.adapterPosition
                notes.remove(deletedNote)
                (recycler.adapter as RecyclerViewAdapter).removeItem(
                    deletedNote,
                    viewHolder.adapterPosition
                )
                notesAndNavigationViewModel.deleteDbNote(deletedNote)
                Snackbar.make(recycler, deletedNote.title.toString(), Snackbar.LENGTH_LONG)
                    .setAction("Undo") {
                        notes.add(position, deletedNote)
                        notesAndNavigationViewModel.insertDbNote(deletedNote)
                        (recycler.adapter as RecyclerViewAdapter).addItem(deletedNote, position)
                    }.show()
            }
        }).attachToRecyclerView(recycler)
    }
    private fun navigateTo(fragment: Fragment) {
        findImplementationOrThrow<FragmentNavigator>()
            .navigateTo(
               fragment
            )
    }
}