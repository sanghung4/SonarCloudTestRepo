import React, { useState } from "react";
import { useAppDispatch,useAppSelector } from "../../hooks/redux";
import {setApiFilters} from "../../store/reducers/UserSlice"
import {
  Button,
  Checkbox,
  Collapse,
  List,
  ListItem,
  ListItemText,
  Typography,
} from "@mui/material";
import { ExpandMore, ChevronRight } from "@mui/icons-material";
import "./Sidebar.css"



const Sidebar = () => {
  const { filters } = useAppSelector((state) => state.userReducer);
  const dispatch = useAppDispatch();
  const [expanded, setExpanded] = useState(false);
  const [openFilters, setOpenFilters] = useState<string[]>([]);


  const toggleFilters = (filterTitle: string) => {
    setOpenFilters((prevState) =>
      prevState.includes(filterTitle)
        ? prevState.filter((title) => title !== filterTitle)
        : [...prevState, filterTitle]
    );
  };

  const isFilterOpen = (filterTitle: string) => openFilters.includes(filterTitle);

  const handleFilterUpdate = (e:any,filter:string,value:string) => {
      dispatch(setApiFilters({filter,value,select:e?.target?.checked}))
  }

  return (
    filters.length > 0 ? 
    <div className="sidebar-wrapper" >
      <Button className="filter-button" onClick={() => setExpanded(!expanded)}>
        {expanded ? (
          <ExpandMore fontSize="large" />
        ) : (
          <ChevronRight fontSize="large" />
        )}
        <Typography >{expanded ? "Hide Filters" : "Show Filters"}</Typography>
        
      </Button>
      <Collapse in={expanded} timeout="auto" >
        <List component="div" disablePadding>
          {filters.map((filter) => (
            <React.Fragment key={filter.title}>
              <hr></hr>
              <ListItem component="div"
                onClick={() => toggleFilters(filter.title)}
                sx={{
                  cursor: "pointer",
                  height: "20px"
                }}
              >
                {isFilterOpen(filter.title) ? (
                    <ExpandMore fontSize="small" />
                  ) : (
                    <ChevronRight fontSize="small" />
                  )}
                <ListItemText primary={filter.title.replaceAll("_"," ")} />
              </ListItem>
              <Collapse in={isFilterOpen(filter.title)} timeout="auto" >
                <List component="div" disablePadding>
                  {filter?.data.map((option) => (
                    <ListItem
                      key={option.value}
                    >
                      <Checkbox name={option.value} value={option.value} onChange={(e) => {handleFilterUpdate(e,filter.title,option.value)} } />
                      <ListItemText  primary={`${option.value} (${option.count})`} />
                    </ListItem>
                  ))}
                </List>
              </Collapse>
            </React.Fragment>
          ))}
        </List>
      </Collapse>
    </div> : <></>
  );
};

export default Sidebar;
