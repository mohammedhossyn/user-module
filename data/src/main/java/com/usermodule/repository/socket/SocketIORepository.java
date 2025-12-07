package com.usermodule.repository.socket;

import com.usermodule.model.socket.SocketIOEntity;
import com.usermodule.model.socket.SocketIOStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListPagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface SocketIORepository extends JpaRepository<SocketIOEntity, Long>, ListPagingAndSortingRepository<SocketIOEntity, Long> {

    List<SocketIOEntity> findByUsernameAndStatus(String username, SocketIOStatus status);
    Optional<SocketIOEntity> findBySessionIdAndServerName(String uuid, String serverName);

    @Transactional
    @Modifying
    @Query(value = " update SocketIOEntity set status = 'INACTIVE' where status <> 'INACTIVE' and serverName = :serverName ")
    void deActiveAllSocket(String serverName);
}