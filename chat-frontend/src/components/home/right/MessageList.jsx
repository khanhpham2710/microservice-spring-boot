import MessageCard from "./MessageCard";

function MessageList({ auth, messages, messageContainerRef }) {

  return (
    <div className="px-10 h-[85vh] overflow-y-scroll pb-10" ref={messageContainerRef}>
      <div className="w-full flex flex-col justify-center items-end  mt-20 py-2">
        {messages?.length > 0 &&
          messages?.map((item, i) => (
            <MessageCard
              key={i}
              isReqUserMessage={item.user.id === auth.reqUser.id}
              content={item.content}
              profilePic={
                "https://media.istockphoto.com/id/521977679/photo/silhouette-of-adult-woman.webp?b=1&s=170667a&w=0&k=20&c=wpJ0QJYXdbLx24H5LK08xSgiQ3zNkCAD2W3F74qlUL0="
              }
            />
          ))}
      </div>
    </div>
  );
}

export default MessageList;
