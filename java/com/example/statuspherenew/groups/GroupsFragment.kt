package com.example.statuspherenew.groups

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import com.example.statuspherenew.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import androidx.recyclerview.widget.RecyclerView
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager


class GroupsFragment : Fragment() {
    private val viewModel: GroupsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_groups, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView: RecyclerView = view.findViewById(R.id.rvGroups)
        val fab: FloatingActionButton = view.findViewById(R.id.fab)

        val sampleGroups = listOf(
            Group(1, "Hiking Buddies", "Enjoy hiking together"),
            Group(2, "Bookworms United", "Group for book lovers"),
            Group(3, "Basketball 5's Team", "Running friendly 5v5's daily"),
            Group(4, "Family", "Sandra, Sarah, Sam, Alyssa"),
            Group(5, "Work Team", "Coworkers for life"),
            Group(6, "Movie Club", "Weekly watching")
        )

        val adapter = GroupsAdapter(sampleGroups)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter

        fab.setOnClickListener {
            showCreateGroupDialog()
        }
    }

    fun showCreateGroupDialog() {
        val layoutInflater = LayoutInflater.from(context)
        val view = layoutInflater.inflate(R.layout.dialog_create_group, null)

        val groupNameInput = view.findViewById<EditText>(R.id.etGroupName)
        val groupDescriptionInput = view.findViewById<EditText>(R.id.etGroupDescription)

        context?.let {
            AlertDialog.Builder(it)
                .setView(view)
                .setTitle("Create New Group")
                .setPositiveButton("Create") { dialog, _ ->
                    val groupName = groupNameInput.text.toString()
                    val groupDescription = groupDescriptionInput.text.toString()
                    Toast.makeText(context, "Group '$groupName' created!", Toast.LENGTH_SHORT)
                        .show()
                    dialog.dismiss()
                }
                .setNegativeButton("Cancel") { dialog, _ ->
                    dialog.cancel()
                }
                .show()
        }
    }


}
