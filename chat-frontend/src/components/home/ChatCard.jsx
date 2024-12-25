const ChatCard = ({ userImg, name, lastMessage }) => {
  const formatTimestamp = (timestamp) => {
    if (!timestamp) return "";

    const options = { year: "numeric", month: "short", day: "numeric" };
    return new Date(timestamp).toLocaleDateString(undefined, options);
  };

  return (
    <div className="flex items-center justify-center py-2 group cursor-pointer">
      <div className="w-[19%]">
        <img className="h-14 w-14 rounded-full" src={userImg || "https://plus.unsplash.com/premium_photo-1664474619075-644dd191935f?q=80&w=2069&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"} alt="profile" />
      </div>
      <div className="pl-5 w-[80%]">
        <div className="flex justify-between items-center">
          <p className="text-lg">{name}</p>
          <p className="text-sm">
            {lastMessage ? formatTimestamp(lastMessage.timestamp) : ""}
          </p>
        </div>
        <div className="flex justify-between items-center">
          <p className="text-gray-600 truncate">
            {lastMessage ? lastMessage.content : ""}
          </p>
          <div className="flex space-x-2">
            <span className="text-gray-500 text-xs">3h</span>
            <span className="bg-green-500 h-2 w-2 rounded-full"></span>
          </div>
        </div>
      </div>
    </div>
  );
};

export default ChatCard;