import { useState } from "react";
import { BsEmojiSmile, BsMicFill } from "react-icons/bs";
import { ImAttachment } from "react-icons/im";
import { useDispatch } from "react-redux";
import { createMessage } from "../../../redux/Message/Action";

function Footer({ currentChat }) {
  const [content, setContent] = useState("");
  const dispatch = useDispatch();
  const token = localStorage.getItem("token");

  const handleCreateNewMessage = () => {
    if (content != "") {
      dispatch(
        createMessage({
          token,
          data: { chatId: currentChat.id, content: content },
        })
      );
      setContent(""); // Clear content after sending
    }
  };

  return (
    <div className="footer bg-[#f0f2f5] absolute bottom-0 w-full py-3 text-2xl">
      <div className="flex justify-between items-center px-5 relative">
        <BsEmojiSmile className="cursor-pointer" />
        <ImAttachment />
        <input
          className="py-2 outline-none border-none bg-white pl-4 rounded-md w-[85%]"
          type="text"
          onChange={(e) => setContent(e.target.value)}
          placeholder="Type message"
          value={content}
          onKeyDown={(e) => {
            if (e.key === "Enter") {
              handleCreateNewMessage();
              setContent("");
            }
          }}
        />
        <BsMicFill />
      </div>
    </div>
  );
}

export default Footer;
