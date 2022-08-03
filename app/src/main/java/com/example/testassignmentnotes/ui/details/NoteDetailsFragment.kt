package com.example.testassignmentnotes.ui.details

import android.os.Bundle
import androidx.fragment.app.activityViewModels
import com.example.testassignmentnotes.R
import com.example.testassignmentnotes.databinding.FragmentNoteDetailsBinding
import com.example.testassignmentnotes.di.DependencyManager
import com.example.testassignmentnotes.ui._base.FragmentNavigator
import com.example.testassignmentnotes.ui._base.ViewBindingFragment
import com.example.testassignmentnotes.ui._base.entities.NoteListItem
import com.example.testassignmentnotes.ui._base.findImplementationOrThrow
import com.example.testassignmentnotes.ui.list.NoteListFragment
import com.example.testassignmentnotes.ui.list.NoteViewModel
import com.example.testassignmentnotes.util.parseToString
import java.time.LocalDateTime

class NoteDetailsFragment : ViewBindingFragment<FragmentNoteDetailsBinding>(
    FragmentNoteDetailsBinding::inflate
) {
    private val noteViewModel: NoteViewModel by activityViewModels()
    private val notesAndNavigationViewModel by lazy { DependencyManager.noteListViewModel() }
    override fun onViewBindingCreated(
        viewBinding: FragmentNoteDetailsBinding,
        savedInstanceState: Bundle?
    ) {
        super.onViewBindingCreated(viewBinding, savedInstanceState)
        noteViewModel.getNote().observe(viewLifecycleOwner) {
            if (it != null) {
                initNoteInfo(it)
            } else {
                initNoteCreate()
            }
        }
        notesAndNavigationViewModel.navigateToNote.observe(
            viewLifecycleOwner
        ) {
            findImplementationOrThrow<FragmentNavigator>()
                .navigateTo(
                    NoteListFragment()
                )

        }
        viewBinding.toolbar.setNavigationOnClickListener {
            notesAndNavigationViewModel.onClick()
        }
        viewBinding.cancelB.setOnClickListener {
            notesAndNavigationViewModel.onClick()
        }
    }

    private fun initNoteInfo(note: NoteListItem) {
        viewBinding?.noteTitleEt?.setText(note.title)
        viewBinding?.noteContentEt?.setText(note.content)
        viewBinding?.noteCreatedTv?.text = note.createdAt?.parseToString()
        viewBinding?.noteModifiedTv?.text = note.modifiedAt?.parseToString()
        viewBinding?.agreeB?.setOnClickListener {
            if (viewBinding?.noteTitleEt?.text != null) {
                notesAndNavigationViewModel.insertDbNote(
                    NoteListItem(
                        note.id,
                        viewBinding?.noteTitleEt?.text?.toString(),
                        viewBinding?.noteContentEt?.text?.toString(),
                        note.createdAt,
                        LocalDateTime.now()
                    )
                )
            }
            notesAndNavigationViewModel.onClick()
        }
    }

    override fun onDestroyView() {
        noteViewModel.updateNote(null)
        super.onDestroyView()
    }

    private fun initNoteCreate() {
        viewBinding?.agreeB?.text = view?.context?.getString(R.string.create)
        viewBinding?.agreeB?.setOnClickListener {
            if (viewBinding?.noteTitleEt?.text != null) {
                notesAndNavigationViewModel.insertDbNote(
                    NoteListItem(
                        null,
                        viewBinding?.noteTitleEt?.text?.toString(),
                        viewBinding?.noteContentEt?.text?.toString(),
                        LocalDateTime.now(),
                        LocalDateTime.now()
                    )
                )
            }
            notesAndNavigationViewModel.onClick()
        }
    }
}