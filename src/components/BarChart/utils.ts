export const barOptions = {
  responsive: true,
  plugins: {
    legend: {
      position: "top" as const,
    },
  },
};

export const defaultBarData = {
  labels: [],
  datasets: [
    {
      label: "Dataset 1",
      data: [],
    },
    {
      label: "Dataset 2",
      data: [],
    },
  ],
};
