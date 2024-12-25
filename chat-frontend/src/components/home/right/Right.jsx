import Footer from './Footer'
import ChatHeader from './ChatHeader'
import MessageList from './MessageList'
import { useEffect, useRef } from 'react';
import { useDispatch } from 'react-redux';
import { getAllMessages } from '../../../redux/Message/Action';

function Chat({currentChat, messages, auth, token}) {
  const dispatch = useDispatch()
  const messageContainerRef = useRef(null)

  useEffect(() => {
    // Scroll to bottom whenever messages change
    if (messageContainerRef.current) {
      messageContainerRef.current.scrollTop =
        messageContainerRef.current.scrollHeight;
    }
  }, [messages]);

    useEffect(() => {
      if (currentChat?.id)
        dispatch(getAllMessages({ chatId: currentChat?.id, token }));
    }, [currentChat, dispatch, token]);

  return (
    <div className="w-[70%] relative  bg-blue-200">
      <ChatHeader currentChat={currentChat} auth={auth}/>
      <MessageList messages={messages} auth={auth} messageContainerRef={messageContainerRef}/>
      <Footer currentChat={currentChat} />
    </div>
  )
}

export default Chat
