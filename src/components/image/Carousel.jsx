import React, { useEffect, useRef, useState } from "react";
import styled from "styled-components";
import PictureArea from "./PictureArea";
import ArrowRight from "../../assets/arrow_right.png";
import ArrowLeft from "../../assets/arrow_left.png";

const CarouselContainer = styled.div`
  display: flex;
  flex-direction: column;
  gap: 10px;
  align-items: center;
  justify-content: center;
  width: ${({ size }) => (size === "large" ? "480px" : "100%")};
`;

const CarouselWrapper = styled.div`
  position: relative;
  width: 100%;
  overflow: hidden;
  border-radius: ${(props) => props.theme.radiuses.radius_m};

  &:hover button {
    display: flex;
    z-index: 1;
  }

  button {
    position: absolute;
    top: 42%;
    padding: 8px;
    display: none;
    background-color: rgba(232, 232, 232, 0.84);
    margin: 8px;
    width: 40px;
    height: 40px;
    border-radius: 50%;
    display: flex;
    align-items: center;
    justify-content: center;

    & > img {
      width: 30px;
      height: 30px;
    }
  }
`;

const ButtonLeft = styled.button`
  left: 0;
`;

const ButtonRight = styled.button`
  right: 0;
`;

const ImgList = styled.ul`
  display: flex;
  width: 100%;
`;

const ThumnailList = styled.div`
  display: flex;
  gap: 10px;
`;

const ThumnailImg = styled.button`
  width: 40px;
  height: 40px;
  overflow: hidden;
  vertical-align: top;
  filter: ${({ $isFocus }) => !$isFocus && "brightness(0.5)"};
`;

const Image = styled.img`
  width: 100%;
  height: 100%;
  object-fit: cover;
`;

const DefaultButton = styled.button`
  width: 7px;
  height: 7px;
  border-radius: 50%;
  background-color: ${({ theme }) => theme.colors.surface_weak};
  filter: ${({ $isFocus }) => !$isFocus && "brightness(0.5)"};
`;

function Carousel({ imgList, thumbnail, size }) {
  const [currentIndex, setCurrentIndex] = useState(1);
  const [curImgList, setCurImgList] = useState([]);
  const [isDragging, setIsDragging] = useState(false);
  const carouselRef = useRef();
  const touchStartX = useRef();
  const touchEndX = useRef();

  useEffect(() => {
    if (imgList.length > 0) {
      const startData = imgList[0];
      const endData = imgList[imgList.length - 1];
      setCurImgList([endData, ...imgList, startData]);
    }
  }, []);

  useEffect(() => {
    carouselRef.current.style.transform = `translateX(-${currentIndex}00%)`;
  }, [currentIndex]);

  // transition 끝난 후 index 변경할 수 있도록 하는 setTimeout 함수
  function moveToNthSlide(index) {
    setTimeout(() => {
      setCurrentIndex(index);
      carouselRef.current.style.transition = ``;
    }, 500);
  }

  // 넘기는 부분 구현
  function handleSwipe(direction) {
    const newIndex = currentIndex + direction;

    // 마지막 부분이면 transition 후 index 변경
    if (newIndex === imgList.length + 1) {
      moveToNthSlide(1);
    } else if (newIndex === 0) {
      moveToNthSlide(imgList.length);
    }

    setCurrentIndex((prev) => prev + direction);
    carouselRef.current.style.transition = "all 0.5s ease-in-out";
  }

  function moveDirect(index) {
    setCurrentIndex(index);
    carouselRef.current.style.transition = "all 0.5s ease-in-out";
  }

  // 터치 시작 시점
  function handleTouchStart(e) {
    e.preventDefault();
    e.stopPropagation();

    setIsDragging(true);
    touchStartX.current = e.clientX;
  }

  // 터치를 하는 동안 translate 효과 부여
  function handleTouchMove(e) {
    e.preventDefault();
    e.stopPropagation();
    if (!isDragging) {
      return;
    }

    const currTouchX = e.clientX;
    carouselRef.current.style.transform = `translateX(calc(-${currentIndex}00% - ${
      (touchStartX.current - currTouchX) * 2 || 0
    }px))`;
  }
  // 터치 끝난 시점
  const handleTouchEnd = (e) => {
    e.preventDefault();
    e.stopPropagation();
    if (!isDragging) {
      return;
    }

    touchEndX.current = e.clientX;

    if (touchStartX.current >= touchEndX.current) {
      handleSwipe(1);
    } else {
      handleSwipe(-1);
    }
    console.log("end:", touchEndX.current);
    setIsDragging(false);
  };

  return (
    <CarouselContainer size={size} onClick={(e) => e.preventDefault()}>
      <CarouselWrapper id="carousel-wrapper">
        <ButtonLeft onClick={() => handleSwipe(-1)}>
          <img src={ArrowLeft} />
        </ButtonLeft>
        <ButtonRight onClick={() => handleSwipe(1)}>
          <img src={ArrowRight} />
        </ButtonRight>
        <ImgList
          ref={carouselRef}
          onMouseDown={handleTouchStart}
          onMouseMove={handleTouchMove}
          onMouseUp={handleTouchEnd}
          onMouseLeave={handleTouchEnd}
        >
          {curImgList.map((i, index) => {
            return (
              <li key={index}>
                <PictureArea img={i} size={size} />
              </li>
            );
          })}
        </ImgList>
      </CarouselWrapper>
      <ThumnailList>
        {imgList.map((item, index) =>
          thumbnail ? (
            <ThumnailImg
              onClick={() => moveDirect(index + 1)}
              $isFocus={currentIndex === index + 1}
              key={index}
            >
              <Image src={item} />
            </ThumnailImg>
          ) : (
            <DefaultButton $isFocus={currentIndex !== index + 1} key={index} />
          )
        )}
      </ThumnailList>
    </CarouselContainer>
  );
}

export default Carousel;
