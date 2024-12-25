import ProfileSection from "./ProfileSection";
import SearchBar from "./SearchBar";
import ChatList from "../ChatList";
import { useDispatch } from "react-redux";
import { createChat } from "../../../redux/Chat/Action";
import { useState } from "react";

function Left({
  auth,
  handleNavigate,
  handleCreateGroup,
  setAnchorEl,
  anchorEl,
  chat,
  lastMessages,
  setCurrentChat,
  token,
}) {
  const [querys, setQuerys] = useState("");
  const dispatch = useDispatch();
  const handleClickOnChatCard = (userId) => {
    dispatch(createChat({ token, data: { userId } }));
  };

  const handleCurrentChat = (item) => {
    setCurrentChat(item);
  };

  return (
    <div className="w-full h-full">
      <ProfileSection
        auth={auth}
        handleNavigate={handleNavigate}
        handleCreateGroup={handleCreateGroup}
        setAnchorEl={setAnchorEl}
        anchorEl={anchorEl}
      />
      <SearchBar querys={querys} setQuerys={setQuerys} />
      <ChatList
        querys={querys}
        auth={auth}
        chat={chat}
        lastMessages={lastMessages}
        handleClickOnChatCard={handleClickOnChatCard}
        handleCurrentChat={handleCurrentChat}
      />
    </div>
  );
}

export default Left;
