import { AiOutlineSearch } from "react-icons/ai";
import { BsThreeDotsVertical } from "react-icons/bs";

function ChatHeader({ currentChat, auth }) {
  const getName = () => {
    if (currentChat?.group) {
      return { name: currentChat.chatName, img: currentChat.chatImage };
    } else {
      const temp = currentChat.users.filter((item) => {
        return item.id != auth.reqUser.id;
      })[0];
      return { name: temp.name, img: temp.profile };
    }
  };

  const { name, img } = getName()

  return (
    <div className="header absolute top-0 w-full bg-[#f0f2f5]">
      <div className="flex justify-between">
        <div className="py-3 space-x-4 flex items-center px-3">
          <img
            className="w-10 h-10 rounded-full"
            src={img || "https://cdn.pixabay.com/photo/2015/08/03/13/58/whatsapp-873316_640.png"}
            alt=""
          />
          <p>{name || "username"}</p>
        </div>
        <div className="flex py-3 space-x-4 items-center px-3">
          <AiOutlineSearch />
          <BsThreeDotsVertical />
        </div>
      </div>
    </div>
  );
}

export default ChatHeader;
