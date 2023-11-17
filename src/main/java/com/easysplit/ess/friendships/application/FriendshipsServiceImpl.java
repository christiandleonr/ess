package com.easysplit.ess.friendships.application;

import com.easysplit.ess.friendships.domain.contracts.FriendshipsRepository;
import com.easysplit.ess.friendships.domain.contracts.FriendshipsService;
import com.easysplit.ess.friendships.domain.models.Friendship;
import com.easysplit.ess.friendships.domain.models.FriendshipEntity;
import com.easysplit.ess.friendships.domain.models.FriendshipsMapper;
import org.springframework.stereotype.Service;

@Service
public class FriendshipsServiceImpl implements FriendshipsService {
    private final FriendshipsRepository friendshipsRepository;
    private final FriendshipsMapper friendshipsMapper;

    public FriendshipsServiceImpl(FriendshipsRepository friendshipsRepository, FriendshipsMapper friendshipsMapper) {
        this.friendshipsRepository = friendshipsRepository;
        this.friendshipsMapper = friendshipsMapper;
    }

    @Override
    public Friendship createFriendship(Friendship friendship) {
        FriendshipEntity createdFriendship = friendshipsRepository.createFriendship(
                friendshipsMapper.toFriendshipEntity(friendship)
        );

        return friendshipsMapper.toFriendship(createdFriendship);
    }
}
