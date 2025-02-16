package dao;

import datasource.dao.PlaylistsDao;
import datasource.datamappers.PlaylistMapper;
import datasource.exceptions.SQLQueryException;
import domain.dto.playlists.ListOfPlaylists;
import domain.dto.playlists.Playlist;
import junit.framework.TestCase;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.*;

public class PlaylistsDaoTest extends TestCase {
    private PlaylistsDao sut;
    private PlaylistMapper mockPlaylistMapper;
    private PreparedStatement mockPreparedStatement;
    private ResultSet mockResultSet;
    private String token;

    public void setUp() {
        mockPlaylistMapper = mock(PlaylistMapper.class);
        mockPreparedStatement = mock(PreparedStatement.class);
        mockResultSet = mock(ResultSet.class);
        sut = spy(new PlaylistsDao());
        sut.setPlaylistMapper(mockPlaylistMapper);
        token = "1234-1234-1234";
    }

    public void testGetAllPlaylistsSuccess() throws Exception {
        // Arrange
        ListOfPlaylists expected = new ListOfPlaylists();
        doReturn(mockPreparedStatement).when(sut).prepareStatement(anyString());
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockPlaylistMapper.mapPlaylistsFromResultSet(mockResultSet, token)).thenReturn(expected);

        // Act
        ListOfPlaylists actual = sut.getAllPlaylists(token);

        // Assert
        assertEquals(expected, actual);
        verify(sut, times(1)).disconnect();
    }

    public void testDeletePlaylistSuccess() throws Exception {
        // Arrange
        String playlistId = "1";
        ListOfPlaylists expected = new ListOfPlaylists();
        doReturn(mockPreparedStatement).when(sut).prepareStatement(anyString());
        when(mockPreparedStatement.execute()).thenReturn(true);
        doReturn(expected).when(sut).getAllPlaylists(token);

        // Act
        ListOfPlaylists actual = sut.deletePlaylist(token, playlistId);

        // Assert
        assertEquals(expected, actual);
        verify(sut, times(1)).disconnect();
    }

    public void testAddPlaylistSuccess() throws Exception {
        // Arrange
        String owner = "1234-1234-1234";
        String playlistName = "New Playlist";
        ListOfPlaylists expected = new ListOfPlaylists();
        doReturn(mockPreparedStatement).when(sut).prepareStatement(anyString());
        when(mockPreparedStatement.execute()).thenReturn(true);
        when(sut.getAllPlaylists(token)).thenReturn(expected);

        // Act
        ListOfPlaylists actual = sut.addPlaylist(token, owner, playlistName);

        // Assert
        assertEquals(expected, actual);
        verify(sut, times(1)).disconnect();
    }

    public void testEditPlaylistSuccess() throws Exception {
        // Arrange
        String playlistId = "1";
        Playlist playlist = new Playlist();
        playlist.setName("updated-name");
        ListOfPlaylists expected = new ListOfPlaylists();
        doReturn(mockPreparedStatement).when(sut).prepareStatement(anyString());
        when(mockPreparedStatement.execute()).thenReturn(true);
        when(sut.getAllPlaylists(token)).thenReturn(expected);

        // Act
        ListOfPlaylists actual = sut.editPlaylist(token, playlistId, playlist);

        // Assert
        assertEquals(expected, actual);
        verify(sut, times(1)).disconnect();
    }

    public void testPlaylistGetRequestThrowsSQLQueryException() throws Exception {
        // Arrange
        doReturn(mockPreparedStatement).when(sut).prepareStatement(anyString());
        when(mockPreparedStatement.executeQuery()).thenThrow(new SQLException());

        // Act & Assert
        assertThrows(SQLQueryException.class, () -> sut.getAllPlaylists(token));

        verify(sut, times(1)).disconnect();
    }
}