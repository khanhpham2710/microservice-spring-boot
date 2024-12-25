import { AiOutlineClose } from "react-icons/ai";
import { useNavigate } from "react-router-dom";
import StatusUserCard from "../components/status/StatusUserCard";

const Status = () => {
  const navigate = useNavigate();

  // Function to navigate back
  const handleNavigate = () => {
    navigate(-1); // Go back to the previous page
  };

  return (
    <div>
      <div className="flex items-center px-[14vw] py-[7vh]">
        {/* Left side part */}
        <div className="left h-[85vh] bg-[#1e262c] lg:w-2/5 w-2/3 px-5">
          <div className="pt-5 h-[13%]">
            <StatusUserCard />
          </div>
          <hr />
          <div className="overflow-y-scroll h-[86%] pt-3">
            {[1, 1, 1, 1, 1,1,1,1,11,1].map((item, index) => (
              <StatusUserCard key={index} />
            ))}
          </div>
        </div>

        {/* Right side part with close button */}
        <div
          onClick={handleNavigate}
          className="right relative h-[85vh] lg:w-[70%] w-[50%] bg-[#0b141a] "
        >
          <AiOutlineClose className="text-white cursor-pointer absolute top-5 right-10 text-xl" />
        </div>
      </div>
    </div>
  );
};

export default Status;