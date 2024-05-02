package com.example.statuspherenew.friends

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.statuspherenew.R

class FriendsFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var friendsAdapter: FriendsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_friends, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.friendsRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)

        val friends = listOf(
            Friend(1, "Sandra", "Wife", R.drawable.friend_1),
            Friend(2, "Sarah", "Daughter", R.drawable.friend_20),
            Friend(3, "Sam", "Son", R.drawable.friend_19),
            Friend(4, "Alyssa", "Daughter", R.drawable.friend_15),
            Friend(5, "Donald", "Friend", R.drawable.friend_16),
            Friend(6, "Shaniya", "Coworker", R.drawable.friend_12),
            Friend(7, "Charlotte", "Friend", R.drawable.friend_5),
            Friend(8, "Jamal", "Friend", R.drawable.friend_17),
            Friend(9, "Thomas", "Coworker", R.drawable.friend_14),
            Friend(10, "Elizabeth", "Coworker", R.drawable.friend_4),
            Friend(11, "Indy", "Friend", R.drawable.friend_13),
            Friend(12, "Nate", "Friend", R.drawable.friend_2),
            Friend(13, "Bryce", "Boss", R.drawable.friend_3),
            Friend(14, "Anna", "Cousin", R.drawable.friend_8),
            Friend(15, "Brittany", "Aunt", R.drawable.friend_9),
            Friend(16, "John", "Uncle", R.drawable.friend_6),
            Friend(17, "Rea", "Cousin", R.drawable.friend_10),
            Friend(18, "Beverly", "Grandma", R.drawable.friend_18),
            Friend(19, "Tim", "Grandpa", R.drawable.friend_7),
            Friend(20, "Zee", "Cousin", R.drawable.friend_11)
        )

        friendsAdapter = FriendsAdapter(friends) { friend ->
        }

        recyclerView.adapter = friendsAdapter
    }

    }

