package com.easysplit.ess.friendships.application;

import com.easysplit.ess.friendships.domain.contracts.FriendshipsRepository;
import com.easysplit.ess.user.domain.contracts.FriendsService;
import com.easysplit.ess.user.domain.models.FriendshipsMapper;
import org.springframework.stereotype.Service;

@Service
public class FriendsServiceImpl implements FriendsService {
    private final FriendshipsRepository friendshipsRepository;
    private final FriendshipsMapper friendshipsMapper;

    public FriendsServiceImpl(FriendshipsRepository friendshipsRepository, FriendshipsMapper friendshipsMapper) {
        this.friendshipsRepository = friendshipsRepository;
        this.friendshipsMapper = friendshipsMapper;
    }

    
}
