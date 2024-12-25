function NoCurrentChat() {
  return (
    <div className="w-[70%] flex flex-col items-center justify-center h-full">
      <div className="max-w-[70%] text-center">
        <img
          className="ml-11 lg:w-[75%] "
          src="https://cdn.pixabay.com/photo/2015/08/03/13/58/whatsapp-873316_640.png"
          alt="whatsapp-icon"
        />
        <h1 className="text-4xl text-gray-600">WhatsApp Web</h1>
        <p className="my-9">
          Send and receive messages with WhatsApp and save time.
        </p>
      </div>
    </div>
  );
}

export default NoCurrentChat;
