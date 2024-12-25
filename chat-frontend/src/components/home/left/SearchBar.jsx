import { AiOutlineSearch } from "react-icons/ai";
import { BsFilter } from "react-icons/bs";
import useDebounce from "../../../config/useDebounce";
import { useEffect } from "react";
import { useDispatch } from "react-redux";
import { searchUser } from "../../../redux/Auth/Action";

const SearchBar = ({ querys, setQuerys }) => {
  const debounceValue = useDebounce(querys, 500);
  const dispatch = useDispatch();
  const token = localStorage.getItem("token");

  useEffect(() => {
    const handleSearch = () => {
      if (debounceValue) {
        dispatch(searchUser({ debounceValue, token }));
      }
    };

    handleSearch();
  }, [debounceValue, dispatch, token]);

  return (
    <div className="relative flex justify-center items-center bg-white py-4 px-3">
      <input
        className="border-none outline-none bg-slate-200 rounded-md w-[93%] pl-9 py-3"
        type="text"
        placeholder="Search or Start new chat"
        onChange={(e) => {
          setQuerys(e.target.value);
        }}
        value={querys}
      />
      <AiOutlineSearch className="left-5 top-8 absolute" />
      <div>
        <BsFilter className="ml-4 text-3xl" />
      </div>
    </div>
  );
};

export default SearchBar;
