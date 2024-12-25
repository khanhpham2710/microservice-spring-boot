import { useEffect, useState } from "react";
import { BsArrowLeft, BsArrowRight } from "react-icons/bs";
import SelectedMember from "./SelectedMember";
import ChatCard from "../home/ChatCard";
import NewGroup from "./NewGroup";
import { useDispatch, useSelector } from "react-redux";
import { searchUser } from "../../redux/Auth/Action";
import useDebounce from "../../config/useDebounce";

const CreateGroup = ({ setIsGroup }) => {
  const [newGroup, setNewGroup] = useState(false);
  // State to store the selected group members
  const [groupMember, setGroupMember] = useState(new Set());
  const [query, setQuery] = useState("");
  const dispatch = useDispatch();
  const { auth } = useSelector((store) => store);
  const token = localStorage.getItem("token");

  // Function to remove a member from the group
  const handleRemoveMember = (item) => {
    const updatedMembers = new Set(groupMember);
    updatedMembers.delete(item);
    setGroupMember(updatedMembers);
  };

  const debounceValue = useDebounce(query, 500);

  useEffect(() => {
    const handleSearch = () => {
      if (debounceValue) {
        dispatch(searchUser({ debounceValue, token }));
      }
    };

    handleSearch();
  }, [debounceValue, dispatch, token]);


  const handleSortMember = (item) => {
    return [...groupMember].some(member => member.id === item.id);
  };

  return (
    <div className="w-full h-full">
      {!newGroup && (
        <div className="h-full">
          {/* Header */}
          <div className="flex items-center p-3 space-x-10 bg-[#069b60] text-white h-[10%]">
            <BsArrowLeft
              className="cursor-pointer text-2xl font-bold"
              onClick={() => {
                setIsGroup(false);
              }}
            />
            <p className="text-xl font-semibold">Add Participants</p>
          </div>

          <div className="relative bg-white py-4 px-3 min-h-[10%] max-h-[20%]">
            {/* Showing and removing group members */}
            <div className="flex space-x-2 flex-wrap space-y-1 h-[50%]">
              {groupMember.size > 0 &&
                Array.from(groupMember).map((item, index) => (
                  <SelectedMember
                    key={index}
                    handleRemoveMember={(item) => handleRemoveMember(item)}
                    member={item}
                  />
                ))}
            </div>

            {/* Adding group members */}
            <input
              type="text"
              className="outline-none border-b border-[#8888] p-2 w-[93%] h-[50%]"
              placeholder="Search user"
              value={query}
              onChange={(e) => {
                setQuery(e.target.value);
              }}
            />
          </div>

          <div className="bg-white overflow-y-scroll max-h-[60%]">
            {query &&
              auth.searchUser?.map((item) => {
                if (!handleSortMember(item) && item.id != auth.reqUser.id) {
                  return (
                    <div
                      onClick={() => {
                        groupMember.add(item);
                        setGroupMember(groupMember);
                        setQuery("");
                      }}
                      key={item?.id}
                    >
                      <hr />
                      <ChatCard userImg={item.profile} name={item.name} />
                    </div>
                  );
                }
              })}
          </div>

          <div className="bottom-10 py-10 bg-slate-200 items-center justify-center flex h-[10%]">
            <div
              onClick={() => {
                setNewGroup(true);
              }}
              className="bg-green-600 rounded-full p-4 cursor-pointer"
            >
              <BsArrowRight className="text-white font-bold text-3xl" />
            </div>
          </div>
        </div>
      )}

      {newGroup && (
        // Render the NewGroup component for creating a new group
        <NewGroup
          groupMember={groupMember}
          setIsGroup={setIsGroup}
          setNewGroup={setNewGroup}
        />
      )}
    </div>
  );
};

export default CreateGroup;
