import { useEffect, useState } from "react";
import "./Homepage.css";
import { useNavigate } from "react-router-dom";
import Profile from "../components/home/left/Profile";
import CreateGroup from "../components/group/CreateGroup";
import { useDispatch, useSelector } from "react-redux";
import { currentUser } from "../redux/Auth/Action";
import { getUsersChat } from "../redux/Chat/Action";
import NoCurrentChat from "../components/home/Right/NoCurrentChat";
import Right from "../components/home/right/Right";
import Left from "../components/home/left/Left";
import SockJS from "sockjs-client";
import { over } from "stompjs";
import { BASE_API_URL } from "../config/api";

function HomePage() {
  const [currentChat, setCurrentChat] = useState(null);
  const [isProfile, setIsProfile] = useState();
  const [isGroup, setIsGroup] = useState();
  const [lastMessages, setLastMessages] = useState({});
  const navigate = useNavigate();
  const dispatch = useDispatch();
  const [anchorEl, setAnchorEl] = useState(null);
  const { auth, chat, message } = useSelector((store) => store);
  const token = localStorage.getItem("token");
  const [messages, setMessages] = useState([]);
  const [stompClient, setStompClient] = useState();
  const [isConnected, setIsConnected] = useState();

  // Function to establish a WebSocket connection
  const connect = () => {
    const sock = new SockJS(`${BASE_API_URL}/ws`);
    const temp = over(sock);
    setStompClient(temp);
    const headers = {
      Authorization: `Bearer ${token}`,
    };
    // Connect to WebSocket server
    temp.connect(headers, onConnect, onError);
  };

  const onError = (error) => {
    console.log("on error ", error);
  };

  // Callback for successful WebSocket connection
  const onConnect = () => {
    setIsConnected(true);

    if (stompClient && currentChat) {
      if (currentChat.group) {
        stompClient.subscribe(`/group/${currentChat?.id}`, onMessageReceive);
      } else {
        stompClient.subscribe(`/user/${currentChat?.id}`, onMessageReceive);
      }
    }
  };

  // Callback to handle received messages from WebSocket
  const onMessageReceive = (payload) => {
    const receivedMessage = JSON.parse(payload.body);
    setMessages((prevMessages) => [...prevMessages, receivedMessage]);
  };

  // Effect to establish a WebSocket connection
  useEffect(() => {
    connect();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  // Effect to subscribe to a chat when connected
  useEffect(() => {
    if (isConnected && stompClient && currentChat?.id) {
      const subscription = currentChat.group
        ? stompClient.subscribe(`/group/${currentChat.id}`, onMessageReceive)
        : stompClient.subscribe(
            `/user/${currentChat.id}/private`,
            onMessageReceive
          );

      return () => {
        subscription.unsubscribe();
      };
    }
  }, [isConnected, stompClient, currentChat]);

  // Effect to handle sending a new message via WebSocket
  useEffect(() => {
    if (message.newMessage && stompClient) {
      stompClient.send("/app/message", {}, JSON.stringify(message.newMessage));
    }
  }, [message.newMessage, stompClient]);

  const handleNavigate = () => {
    setIsProfile(true);
    setAnchorEl(null);
  };

  const handleCloseOpenProfile = () => {
    setIsProfile(false);
  };

  const handleCreateGroup = () => {
    setIsGroup(true);
    setAnchorEl(null);
  };

  useEffect(() => {
    dispatch(currentUser(token));
  }, [dispatch, token]);

  useEffect(() => {
    dispatch(getUsersChat({ token }));
  }, [chat.createdChat, chat.createdGroup, dispatch, token]);

  useEffect(() => {
    if (message.messages) {
      setMessages(message.messages);
    }
  }, [message.messages]);

  useEffect(() => {
    if (!auth.reqUser) {
      navigate("/signin");
    }
  }, [auth.reqUser, navigate]);

  useEffect(() => {
    const prevLastMessages = { ...lastMessages };
    if (message.messages && message.messages.length > 0) {
      message.messages.forEach((msg) => {
        prevLastMessages[msg.chat.id] = msg;
      });
      setLastMessages({ ...prevLastMessages });
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [message?.messages]);

  return (
    <div className="relative">
      <div className="w-[100vw] py-14 bg-[#00a884]">
        <div className="flex bg-[#f0f2f5] h-[90vh] absolute top-[5vh] left-[2vw] w-[96vw] overflow-hidden">
          <div className="left w-[30%] h-full bg-[#e8e9ec]">
            {isProfile && (
              <Profile handleCloseOpenProfile={handleCloseOpenProfile} />
            )}
            {isGroup && <CreateGroup setIsGroup={setIsGroup} />}
            {!isProfile && !isGroup && (
              <Left
                auth={auth}
                handleNavigate={handleNavigate}
                handleCreateGroup={handleCreateGroup}
                setAnchorEl={setAnchorEl}
                anchorEl={anchorEl}
                chat={chat}
                lastMessages={lastMessages}
                setCurrentChat={setCurrentChat}
                token={token}
              />
            )}
          </div>
          {!currentChat && <NoCurrentChat />}
          {currentChat && (
            <Right
              auth={auth}
              messages={messages}
              currentChat={currentChat}
              token={token}
            />
          )}
        </div>
      </div>
    </div>
  );
}

export default HomePage;
